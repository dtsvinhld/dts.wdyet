package vn.com.dtsgroup.wdyet.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import vn.com.dtsgroup.wdyet.Activities.Profile.Account.ChangeFNActivity;
import vn.com.dtsgroup.wdyet.Activities.Profile.Account.ChangePwActivity;
import vn.com.dtsgroup.wdyet.Activities.Profile.Account.SafeActivity;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/18/2018.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private CircleImageView img_user_um;
    private TextView txt_fn_um, txt_un_um;
    private Button btn_um_fn, btn_um_pw, btn_um_safe, btn_um_lo;
    private SharedPreferences sP;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile, container, false);

        sP = getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        btn_um_fn = (Button) view.findViewById(R.id.btn_um_fn);
        btn_um_fn.setOnClickListener(this);
        btn_um_pw = (Button) view.findViewById(R.id.btn_um_pw);
        btn_um_pw.setOnClickListener(this);
        btn_um_safe = (Button) view.findViewById(R.id.btn_um_safe);
        btn_um_safe.setOnClickListener(this);
        btn_um_lo = (Button) view.findViewById(R.id.btn_um_lo);
        btn_um_lo.setOnClickListener(this);
        txt_fn_um = (TextView) view.findViewById(R.id.txt_fullname_um);
        txt_fn_um.setText(sP.getString("fullname","Họ tên"));
        txt_un_um = (TextView) view.findViewById(R.id.txt_username_um);
        txt_un_um.setText(sP.getString("username","username"));

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_um_fn:
                startActivity(new Intent(getContext(), ChangeFNActivity.class));
                break;

            case R.id.btn_um_pw:
                startActivity(new Intent(getContext(), ChangePwActivity.class));
                break;

            case R.id.btn_um_safe:
                startActivity(new Intent(getContext(), SafeActivity.class));
                break;

            case R.id.btn_um_lo:
                final Dialog dialog = new Dialog(getContext());
                dialog.setTitle("Đăng xuất");
                dialog.setContentView(R.layout.dialog_logout);
                dialog.show();
                ((Button) dialog.findViewById(R.id.btn_yes)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = sP.edit();
                        editor.clear();
                        editor.commit();
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
                ((Button) dialog.findViewById(R.id.btn_no)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                break;
        }
    }
}
