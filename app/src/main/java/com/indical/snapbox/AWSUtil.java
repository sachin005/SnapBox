package com.indical.snapbox;

import android.content.Context;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.internal.*;

/**
 * Created by sachin on 6/17/16.
 *
 * Utility class for AWS operations
 */
public class AWSUtil {
    private static TransferUtility mTransferUtility;
    private static AmazonS3Client mS3Client;
    private static CognitoCachingCredentialsProvider mCredProvider;

    public static CognitoCachingCredentialsProvider getCredProvider(Context context) {
        if(mCredProvider == null) {
            mCredProvider = new CognitoCachingCredentialsProvider(context.getApplicationContext(),
                    Constants.COGNITO_POOL_ID, Regions.US_EAST_1);
        }
        return mCredProvider;
    }

    public static AmazonS3Client getS3Client(Context context) {
        if(mS3Client == null) {
            mS3Client = new AmazonS3Client(getCredProvider(context));
        }
        return mS3Client;
    }

    public static TransferUtility getTransferUtility(Context context) {
        if(mTransferUtility == null) {
            mTransferUtility = new TransferUtility(getS3Client(context),
                    context.getApplicationContext());
        }
        return mTransferUtility;
    }
}
