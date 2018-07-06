package com.example.androidtrain.print;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.support.annotation.RequiresApi;

/**
 * Created by lizz on 2018/7/6.
 */

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
class MyPrintDocumentAdapter extends PrintDocumentAdapter {
    private Activity mActivity;
    private PrintedPdfDocument mPdfDocument;
    private int totalPages;
    public MyPrintDocumentAdapter(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        mPdfDocument = new PrintedPdfDocument(mActivity, newAttributes);

        //Respond to cancellation request
        if (cancellationSignal.isCanceled()){
            callback.onLayoutCancelled();
            return;
        }

        //Compute the expected number of printed pages
        int pages = computePageCount(newAttributes);
        totalPages = pages;

        if (pages > 0){
            PrintDocumentInfo info = new PrintDocumentInfo.Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(pages)
                    .build();

            callback.onLayoutFinished(info, true);
        }else {
            callback.onLayoutFailed("Page count calculation failed.");
        }
    }

    private int computePageCount(PrintAttributes newAttributes) {
        int itemPerPage = 4;//default item count for protrait mode

        PrintAttributes.MediaSize pageSize = newAttributes.getMediaSize();
        if (!pageSize.isPortrait()){
            //Six items per page in landscape orientation
            itemPerPage = 6;
        }

        //Determine number of print items
        int printItemCount = getPrintItemCount();
        return (int)Math.ceil(printItemCount/itemPerPage);
    }

    private int getPrintItemCount() {
        return 20;
    }

    @Override
    public void onWrite(PageRange[] pages, ParcelFileDescriptor destination, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        //Iterate over each page of the document
        //check if it's in the output range.
        for (int i = 0; i < totalPages; i++){
            if (containsPage(pages, i)){

            }
        }
        callback.onWriteFinished(pages);
    }

    private boolean containsPage(PageRange[] pages, int i) {
        return i < pages.length;
    }

    @Override
    public void onFinish() {
        super.onFinish();
    }
}
