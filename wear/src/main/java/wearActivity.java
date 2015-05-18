/**
 * Created by Amy on 5/10/2015.
 */

import android.app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.CapabilityInfo;

import java.util.Set;

import amyphan.watchfinder.R;

public class wearActivity extends Activity implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,
        CapabilityApi.CapabilityListener {
    private static final String CONFIRMATION_HANDLER_CAPABILITY_NAME = "confirmation_handler";
    private static final String TAG = "watchFinder";

    private GoogleApiClient mGoogleApiClient;
    private Node mConfirmationHandlerNode;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        setContentView(R.layout.activity_wear);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .build();
    }
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        if (mGoogleApiClient.isConnected()) {
            Wearable.CapabilityApi.removeCapabilityListener(mGoogleApiClient, this,
                    CONFIRMATION_HANDLER_CAPABILITY_NAME);
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Failed to connect to Google Api Client");
        mConfirmationHandlerNode = null;
    }
    private void sendMessageToCompanion(final String path) {
        if (mConfirmationHandlerNode != null) {
            Wearable.MessageApi.sendMessage(mGoogleApiClient, mConfirmationHandlerNode.getId(),
                    path, new byte[0])
                    .setResultCallback(getSendMessageResultCallback(mConfirmationHandlerNode));
        } else {
            Toast.makeText(this, R.string.no_device_found, Toast.LENGTH_SHORT).show();
        }
    }

    private ResultCallback<MessageApi.SendMessageResult> getSendMessageResultCallback(
            final Node node) {
        return new ResultCallback<MessageApi.SendMessageResult>() {
            @Override
            public void onResult(MessageApi.SendMessageResult sendMessageResult) {
                if (!sendMessageResult.getStatus().isSuccess()) {
                    Log.e(TAG, "Failed to send message with status "
                            + sendMessageResult.getStatus());
                } else {
                    Log.d(TAG, "Sent confirmation message to node " + node.getDisplayName());
                }
            }
        };
    }

    private void setupConfirmationHandlerNode() {
        Wearable.CapabilityApi.addCapabilityListener(
                mGoogleApiClient, this, CONFIRMATION_HANDLER_CAPABILITY_NAME);

        Wearable.CapabilityApi.getCapability(
                mGoogleApiClient, CONFIRMATION_HANDLER_CAPABILITY_NAME,
                CapabilityApi.FILTER_REACHABLE).setResultCallback(
                new ResultCallback<CapabilityApi.GetCapabilityResult>() {
                    @Override
                    public void onResult(CapabilityApi.GetCapabilityResult result) {
                        if (!result.getStatus().isSuccess()) {
                            Log.e(TAG, "setupConfirmationHandlerNode() Failed to get capabilities, "
                                    + "status: " + result.getStatus().getStatusMessage());
                            return;
                        }
                        updateConfirmationCapability(result.getCapability());
                    }
                });
    }

    private void updateConfirmationCapability(CapabilityInfo capabilityInfo) {
        Set<Node> connectedNodes = capabilityInfo.getNodes();
        if (connectedNodes.isEmpty()) {
            mConfirmationHandlerNode = null;
        } else {
            mConfirmationHandlerNode = pickBestNode(connectedNodes);
        }
    }
    private Node pickBestNode(Set<Node> connectedNodes) {
        Node best = null;
        if (connectedNodes != null) {
            for (Node node : connectedNodes) {
                if (node.isNearby()) {
                    return node;
                }
                best = node;
            }
        }
        return best;
    }

    @Override
    public void onConnected(Bundle bundle) {
        setupConfirmationHandlerNode();
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mConfirmationHandlerNode = null;
    }

    @Override
    public void onCapabilityChanged(CapabilityInfo capabilityInfo) {
        updateConfirmationCapability(capabilityInfo);
    }
}
