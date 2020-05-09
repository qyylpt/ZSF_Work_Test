package com.zsf.fingerprint.m_fingerprint_unlock;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.fragment.app.Fragment;

import com.zsf.fingerprint.R;
import com.zsf.fingerprint.wrapper.ApiFingerprintWrapper;

/**
 * @author zsf
 * @date 2019/11/9
 * @Usage
 */
public class FingerprintContentFragment extends Fragment {
    private Button deleteFingerprintButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        deleteFingerprintButton = view.findViewById(R.id.delete_fingerprint_button);
        deleteFingerprintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiFingerprintWrapper.getInstance().wipeFingerprint(getActivity());
                ((FingerprintActivity)getActivity()).resetFingerprint();
            }
        });
        return view;
    }
}
