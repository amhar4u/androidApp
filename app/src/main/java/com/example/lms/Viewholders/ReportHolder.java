package com.example.lms.Viewholders;

import android.Manifest;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Classes.ReportClass;
import com.example.lms.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportHolder extends RecyclerView.ViewHolder {
    private CircleImageView profilePicImageView;
    private ImageButton more;
    private TextView empIdTextView, leaveTextView, balanceTextView, nopayTextView, pendingTextView, approveTextview, rejectTextview, casualTextview, shortTextview, sickTextview;
    private List<ReportClass> reportList;
    private Context context;

    private ReportClass currentReport;

    public ReportHolder(@NonNull View itemView, Context context) {
        super(itemView);
        if (itemView == null) {

            return; }
        this.context = context;
        profilePicImageView = itemView.findViewById(R.id.imageViewProfilePic);
        empIdTextView = itemView.findViewById(R.id.textViewEmpId);
        leaveTextView = itemView.findViewById(R.id.textViewleaves);
        balanceTextView = itemView.findViewById(R.id.textViewbalance);
        nopayTextView = itemView.findViewById(R.id.textViewnopay);
        approveTextview = itemView.findViewById(R.id.textViewapprove);
        rejectTextview = itemView.findViewById(R.id.textViewreject);
        pendingTextView = itemView.findViewById(R.id.textViewpending);
        casualTextview = itemView.findViewById(R.id.textViewcasual);
        shortTextview = itemView.findViewById(R.id.textViewShort);
        sickTextview = itemView.findViewById(R.id.textViewsick);
        more = itemView.findViewById(R.id.buttonDownload);

        if (more != null) {
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPopupMenu(view);
                }
            });
        }

    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    // ... (other cases)

                    case R.id.menu_download:
                        downloadReport(currentReport); // Use the class-level variable here
                        Toast.makeText(view.getContext(), "Download clicked", Toast.LENGTH_SHORT).show();
                        return true;

                    // ... (other cases)
                }
                return false;
            }
        });
        popupMenu.show();
    }




    public void bindData(ReportClass report) {
        currentReport = report;

        empIdTextView.setText(report.getEmpId());
        leaveTextView.setText(String.valueOf(report.getTotalLeaveDays()));
        balanceTextView.setText(String.valueOf(report.getBalanceLeave()));
        nopayTextView.setText(String.valueOf(report.getNoPayDays()));
        approveTextview.setText(String.valueOf(report.getApprovedLeaveDays()));
        rejectTextview.setText(String.valueOf(report.getRejectedLeaveDays()));
        pendingTextView.setText(String.valueOf(report.getPendingLeaveDays()));
        casualTextview.setText(String.valueOf(report.getCasualLeaveCount()));
        sickTextview.setText(String.valueOf(report.getSickLeaveCount()));
        shortTextview.setText(String.valueOf(report.getShortLeaveCount()));


        String profilePicUrl = report.getProfilePicUrl();
        if (profilePicUrl != null && !profilePicUrl.isEmpty()) {
            Picasso.get().load(profilePicUrl).into(profilePicImageView);
        } else {

            Picasso.get().load(R.drawable.download).into(profilePicImageView);
        }

    }

    private void downloadReport(ReportClass report) {
        // Implement the logic to download the report as PDF here

        // For example, you can use iTextPDF library to create a PDF
        String pdfFilePath = createPdfForReport(report);

        // Check if the PDF was created successfully
        if (pdfFilePath != null) {
            // Show a toast to indicate the PDF has been downloaded
            Toast.makeText(context, "Report downloaded as PDF: " + pdfFilePath, Toast.LENGTH_SHORT).show();
        } else {
            // Handle the case where PDF creation failed
            Toast.makeText(context, "Failed to create the PDF report.", Toast.LENGTH_SHORT).show();
        }
    }


    private String createPdfForReport(ReportClass report) {
        String pdfFilePath;
        try {
            // Check if the app has the necessary permission to write to external storage
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Request the permission if it has not been granted yet
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return null; // Return null for now, the actual PDF creation will happen after permission is granted
            }

            // Create a PDF file using iTextPDF
            Document document = new Document();
            pdfFilePath = Environment.getExternalStorageDirectory().getPath() + "/report_" + report.getEmpId() + ".pdf";
            PdfWriter.getInstance(document, new FileOutputStream(pdfFilePath));
            document.open();

            // Add the profile picture to the document

            // If there is no profile picture, proceed without adding the image
            document.add(new Paragraph("Employee ID: " + report.getEmpId()));
            document.add(new Paragraph("Total Leaves: " + report.getTotalLeaveDays()));
            document.add(new Paragraph("Balance Leaves:" + report.getBalanceLeave()));
            document.add(new Paragraph("Nopay Leaves: " + report.getNoPayDays()));
            document.add(new Paragraph("Sick Leaves:" + report.getSickLeaveCount()));
            document.add(new Paragraph("Casual   Leaves: " + report.getCasualLeaveCount()));
            document.add(new Paragraph("Short Leaves:" + report.getShortLeaveCount()));
            document.add(new Paragraph("Pending Requests: " + report.getPendingLeaveDays()));
            document.add(new Paragraph("Approved Requests:" + report.getApprovedLeaveDays()));
            document.add(new Paragraph("Rejected Requests:" + report.getRejectedLeaveDays()));
            // Add other details as needed
            // Add other details as needed

            document.close();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            // Log the specific error message
            Log.e("PDF_CREATION_ERROR", "Error creating PDF: " + e.getMessage());
            return null;
        }


        // Return the PDF file path here, outside of the try block
        return pdfFilePath;
    }

        public void onRequestPermissionsResult(int requestCode, int[] grantResults) {
            if (requestCode == 1) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, call createPdfForReport again to create the PDF
                    String pdfFilePath = createPdfForReport(currentReport);
                    if (pdfFilePath != null) {
                        // Show a toast to indicate the PDF has been downloaded
                        Toast.makeText(context, "Report downloaded as PDF: " + pdfFilePath, Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle the case where PDF creation failed
                        Toast.makeText(context, "Failed to create the PDF report.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Permission denied, show a message or take appropriate action
                    Toast.makeText(context, "Permission denied. Cannot create PDF.", Toast.LENGTH_SHORT).show();
                }
            }
        }

        // ... (other code in the ReportHolder class)
    }

