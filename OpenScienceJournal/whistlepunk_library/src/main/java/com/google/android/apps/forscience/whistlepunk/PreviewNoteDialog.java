/*
 *  Copyright 2016 Google Inc. All Rights Reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.google.android.apps.forscience.whistlepunk;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.apps.forscience.whistlepunk.metadata.Label;
import com.google.android.apps.forscience.whistlepunk.metadata.PictureLabel;
import com.google.android.apps.forscience.whistlepunk.metadata.TextLabel;

/**
 * For viewing existing notes.
 */
public class PreviewNoteDialog extends DialogFragment {
    public static final String TAG = "preview_note_dialog";
    public static final String KEY_SAVED_LABEL = "keySavedLabel";

    private Label mLabel;

    public static PreviewNoteDialog newInstance(Label label) {
        PreviewNoteDialog dialog = new PreviewNoteDialog();
        Bundle args = new Bundle();
        args.putParcelable(KEY_SAVED_LABEL, label);
        dialog.setArguments(args);
        return dialog;
    }

    public PreviewNoteDialog() {
    }

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        mLabel = getArguments().getParcelable(KEY_SAVED_LABEL);

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        LinearLayout rootView = (LinearLayout) LayoutInflater.from(getActivity()).inflate(
                R.layout.run_review_label_preview, null);
        alertDialog.setView(rootView);

        ImageView imageView = (ImageView) rootView.findViewById(R.id.picture_note_preview_image);
        final TextView previewText = (TextView) rootView.findViewById(R.id.preview_note_text);

        if (mLabel instanceof PictureLabel) {
            imageView.setVisibility(View.VISIBLE);
            previewText.setVisibility(View.GONE);
            Glide.with(getActivity())
                    .load(((PictureLabel) mLabel).getFilePath())
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PictureUtils.launchExternalViewer(getActivity(),
                            ((PictureLabel) mLabel).getFilePath());
                }
            });
        } else if (mLabel instanceof TextLabel) {
            imageView.setVisibility(View.GONE);
            previewText.setVisibility(View.VISIBLE);
            previewText.setText(((TextLabel) mLabel).getText());
        }
        AlertDialog dialog = alertDialog.create();
        return dialog;
    }
}
