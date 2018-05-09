package vn.com.dtsgroup.wdyet.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import vn.com.dtsgroup.wdyet.Class.Material;
import vn.com.dtsgroup.wdyet.Class.Module;
import vn.com.dtsgroup.wdyet.R;

/**
 * Created by Verdant on 4/16/2018.
 */

public class MaterialAdapter extends ArrayAdapter<ArrayList<Material>> {
    public MaterialAdapter(@NonNull Context context, int resource, @NonNull List<ArrayList<Material>> objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = LayoutInflater.from(getContext()).inflate(R.layout.item_mat, null);

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_element_mat);
        img_dialog_em = (ImageView) dialog.findViewById(R.id.img_dialog_em);
        txt_em = (TextView) dialog.findViewById(R.id.txt_ele_mat);
        txt_em_1 = (TextView) dialog.findViewById(R.id.txt_ele_mat_1);
        txt_em_2 = (TextView) dialog.findViewById(R.id.txt_ele_mat_2);
        txt_em_3 = (TextView) dialog.findViewById(R.id.txt_ele_mat_3);
        txt_em_4 = (TextView) dialog.findViewById(R.id.txt_ele_mat_4);
        txt_em_5 = (TextView) dialog.findViewById(R.id.txt_ele_mat_5);

        final ArrayList<Material> materials = getItem(position);
        if(materials != null){
            if(materials.size() > 0){
                final Material material = materials.get(0);
                item_mat = (LinearLayout) view.findViewById(R.id.item_mat);
                img_item_mat = (ImageView) view.findViewById(R.id.img_item_mat);
                txt_item_mat = (TextView) view.findViewById(R.id.txt_item_mat);
                Picasso.with(getContext()).load(Module.IMAGELINK + material.getImage()).into(img_item_mat);
                txt_item_mat.setText(material.getName());
                item_mat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setTitle(material.getName());
                        Picasso.with(getContext()).load(Module.IMAGELINK + material.getImage()).into(img_dialog_em);

                        txt_em.setText(material.getElements().get(0).getName() + ": " + material.getElements().get(0).getSoluong() + " " + material.getElements().get(0).getDonvi());
                        txt_em_1.setText(material.getElements().get(1).getName() + ": " + material.getElements().get(1).getSoluong() + " " + material.getElements().get(1).getDonvi());
                        txt_em_2.setText(material.getElements().get(2).getName() + ": " + material.getElements().get(2).getSoluong() + " " + material.getElements().get(2).getDonvi());
                        txt_em_3.setText(material.getElements().get(3).getName() + ": " + material.getElements().get(3).getSoluong() + " " + material.getElements().get(3).getDonvi());
                        txt_em_4.setText(material.getElements().get(4).getName() + ": " + material.getElements().get(4).getSoluong() + " " + material.getElements().get(4).getDonvi());
                        txt_em_5.setText(material.getElements().get(5).getName() + ": " + material.getElements().get(5).getSoluong() + " " + material.getElements().get(5).getDonvi());

                        dialog.show();
                    }
                });
            }

            item_mat_1 = (LinearLayout) view.findViewById(R.id.item_mat_1);
            if(materials.size() > 1){
                final Material material = materials.get(1);
                img_item_mat_1 = (ImageView) view.findViewById(R.id.img_item_mat_1);
                txt_item_mat_1 = (TextView) view.findViewById(R.id.txt_item_mat_1);
                Picasso.with(getContext()).load(Module.IMAGELINK + material.getImage()).into(img_item_mat_1);
                txt_item_mat_1.setText(material.getName());
                item_mat_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setTitle(material.getName());
                        Picasso.with(getContext()).load(Module.IMAGELINK + material.getImage()).into(img_dialog_em);

                        txt_em.setText(material.getElements().get(0).getName() + ": " + material.getElements().get(0).getSoluong() + " " + material.getElements().get(0).getDonvi());
                        txt_em_1.setText(material.getElements().get(1).getName() + ": " + material.getElements().get(1).getSoluong() + " " + material.getElements().get(1).getDonvi());
                        txt_em_2.setText(material.getElements().get(2).getName() + ": " + material.getElements().get(2).getSoluong() + " " + material.getElements().get(2).getDonvi());
                        txt_em_3.setText(material.getElements().get(3).getName() + ": " + material.getElements().get(3).getSoluong() + " " + material.getElements().get(3).getDonvi());
                        txt_em_4.setText(material.getElements().get(4).getName() + ": " + material.getElements().get(4).getSoluong() + " " + material.getElements().get(4).getDonvi());
                        txt_em_5.setText(material.getElements().get(5).getName() + ": " + material.getElements().get(5).getSoluong() + " " + material.getElements().get(5).getDonvi());

                        dialog.show();
                    }
                });
            }
            else{
                item_mat_1.setEnabled(false);
            }

            item_mat_2 = (LinearLayout) view.findViewById(R.id.item_mat_2);
            if(materials.size() > 2){
                final Material material = materials.get(2);
                img_item_mat_2 = (ImageView) view.findViewById(R.id.img_item_mat_2);
                txt_item_mat_2 = (TextView) view.findViewById(R.id.txt_item_mat_2);
                Picasso.with(getContext()).load(Module.IMAGELINK + material.getImage()).into(img_item_mat_2);
                txt_item_mat_2.setText(material.getName());
                item_mat_2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.setTitle(material.getName());
                        Picasso.with(getContext()).load(Module.IMAGELINK + material.getImage()).into(img_dialog_em);

                        txt_em.setText(material.getElements().get(0).getName() + ": " + material.getElements().get(0).getSoluong() + " " + material.getElements().get(0).getDonvi());
                        txt_em_1.setText(material.getElements().get(1).getName() + ": " + material.getElements().get(1).getSoluong() + " " + material.getElements().get(1).getDonvi());
                        txt_em_2.setText(material.getElements().get(2).getName() + ": " + material.getElements().get(2).getSoluong() + " " + material.getElements().get(2).getDonvi());
                        txt_em_3.setText(material.getElements().get(3).getName() + ": " + material.getElements().get(3).getSoluong() + " " + material.getElements().get(3).getDonvi());
                        txt_em_4.setText(material.getElements().get(4).getName() + ": " + material.getElements().get(4).getSoluong() + " " + material.getElements().get(4).getDonvi());
                        txt_em_5.setText(material.getElements().get(5).getName() + ": " + material.getElements().get(5).getSoluong() + " " + material.getElements().get(5).getDonvi());

                        dialog.show();
                    }
                });
            }
            else{
                item_mat_2.setEnabled(false);
            }
        }
        return view;
    }

    private Dialog dialog;
    private ImageView img_dialog_em;
    private TextView txt_em, txt_em_1, txt_em_2, txt_em_3, txt_em_4, txt_em_5;

    private LinearLayout item_mat, item_mat_1, item_mat_2;
    private ImageView img_item_mat, img_item_mat_1, img_item_mat_2;
    private TextView txt_item_mat, txt_item_mat_1, txt_item_mat_2;
}
