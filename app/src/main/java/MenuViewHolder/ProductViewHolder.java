package MenuViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rrgroup.ProductList;
import com.example.rrgroup.R;

import Interface.ItemClickListener;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView product_name;
    public ImageView product_image;

    private ItemClickListener itemClickListener;



    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_name = (TextView)itemView.findViewById(R.id.product_name);
        product_image = (ImageView)itemView.findViewById(R.id.product_image);

        itemView.setOnClickListener(this);


    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }


    @Override
    public void onClick(View view) {
       itemClickListener.onClick(view,getAdapterPosition(),false);

    }
}
