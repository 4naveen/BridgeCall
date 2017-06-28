package com.project.lorvent.bridgecall.fragments;


import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.project.lorvent.bridgecall.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutUsFragment extends Fragment {

    TextView textViewTermsAndConditions,textViewNeedHelp,textViewProfile;
    String termsAndConditions;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_about_us, container, false);
        textViewTermsAndConditions=(TextView)view.findViewById(R.id.tv_terms_and_conditions);
        textViewProfile=(TextView)view.findViewById(R.id.view_profile);
        textViewProfile.setPaintFlags(textViewProfile.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        termsAndConditions="The Intellectual Property disclosure will inform users that your website, logo and visuals and other content you created is your property and protected by copyright laws.\n" +
                "\n" +
                "A Termination clause will inform users that their accounts on your website (or their access to your website, if they don’t have an account) can be terminated in case of abuses.\n" +
                "\n" +
                "A Governing Law informs users which country’s laws governs the agreement. This is the country in which your company is headquartered or the country from which you operate your web site.\n" +
                "\n" +
                "A Links To Other Web Sites clause informs users that you are not responsible for any third party web sites that you link to from your website, and that users are responsible for reading these third parties’ own Terms and Conditions or Privacy Policies.\n"+
                "\n" +
                "This app helps making bridge calling easier than before and you don't need to memorize any numbers.\n" +
                "\n" +
                "However this is not a free calling app and you will still charged by your carrier and bridge call (long distance calling) provider.";

        textViewTermsAndConditions.setText(termsAndConditions);

/*        textViewNeedHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), HelpViewpager.class);
                startActivity(intent);
            }
        });*/

        textViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://codecanyon.net/user/jyostna"));
                startActivity(intent);
            }
        });
        return view;
    }

}
