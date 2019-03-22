package com.example.cs160_sp18.prog3;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.util.ArrayList;

public class CardsAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private ArrayList<CardsView> mCardsView;

    public CardsAdapter(Context context, ArrayList<CardsView> cardsView) {
        mContext = context;
        mCardsView = cardsView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // here, we specify what kind of view each cell should have. In our case, all of them will have a view
        // made from card_cell_layout
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_cell_layout, parent, false);
        return new CardViewHolder(view);
    }


    // - get element from your dataset at this position
    // - replace the contents of the view with that element
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // here, we the comment that should be displayed at index `position` in our recylcer view
        // everytime the recycler view is refreshed, this method is called getItemCount() times (because
        // it needs to recreate every cell).
        CardsView card = mCardsView.get(position);
        ((CardViewHolder) holder).bind(card);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mCardsView.size();
    }
}

class CardViewHolder extends RecyclerView.ViewHolder {

    // each data item is just a string in this case
    public RelativeLayout mCardLayout;
    public TextView mTitle;
    public TextView mDistance;
    public ImageView mImage;

    public CardViewHolder(View itemView) {
        super(itemView);
        mCardLayout = itemView.findViewById(R.id.card_cell_layout);
        mTitle = mCardLayout.findViewById(R.id.title);
        mDistance = mCardLayout.findViewById(R.id.distance);
        mImage = mCardLayout.findViewById(R.id.image);
    }

    void bind(CardsView card) {
        mTitle.setText(card.getTitle());
        mDistance.setText(card.getDistance());
        mImage.setImageResource(card.getImageUri());
    }
}