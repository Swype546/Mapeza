package be.ecam.mapeza.mapeza;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 13069 on 28-03-17.
 */

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemAdapterViewHolder>{

    //private ArrayList<Weather> mData = null;
    private String[] mData = null;
    private ItemAdapterOnClickHandler clickHandler;

    public ItemAdapter(ItemAdapterOnClickHandler clickHandler) {
        this.clickHandler = clickHandler;
    }

    public interface ItemAdapterOnClickHandler {
        void onClick(int index);
    }

    //Gestion du viewHolder: creation d'un RecyclerView
    public class ItemAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView element1;
        public final TextView element2;
        public final TextView element3;
        public ItemAdapterViewHolder(View view) {
            super(view);
            element1 = (TextView) view.findViewById(R.id.elem1);
            element2 = (TextView) view.findViewById(R.id.elem2);
            element3 = (TextView) view.findViewById(R.id.elem3);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            clickHandler.onClick(adapterPosition);
        }
    }

    @Override
    public ItemAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.activity_near_element_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem,
                viewGroup, shouldAttachToParentImmediately);
        return new ItemAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemAdapterViewHolder itemAdapterViewHolder, int position) {

        //if(!mData.moveToPosition(position)) return;
        itemAdapterViewHolder.element1.setText("Element" + (position+1));
        itemAdapterViewHolder.element2.setText("Another line to describe");
        itemAdapterViewHolder.element3.setText("last line to describe ");

    }

    @Override
    public int getItemCount() {
        if (null == mData) return 0;
        return mData.length;
    }

    public void setData(String[] data) {
        mData = data;
        notifyDataSetChanged();
    }
}
