package com.example.android.myzomato.detail;


import android.content.Context;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.myzomato.R;

import java.util.List;


public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ForecastAdapterViewHolder> {

    private List<List<String>> menuData;
    private Context context;

    /**
     * Rozhranie, ktore urcuje, co sa vykona po kliknuti na konkretny view
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(Movie weatherForDay);
    }

    public MenuAdapter(Context context) {
        this.context = context;         // for Picasso
    }

    /**
     * Cache of the children views for a forecast list item.
     * Tato mala trieda obsahuje v sebe vsetko co ma obsahovat kazdy jednotlivy view
     * Vytvori a inicializuje ich
     * Tiez implementuje rozhranie, ktore umozni click (tak ako v main), ale tu sa nastavuju data (pozicia, movie)
     */
    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder{

        public final TextView text;
        public final TextView text2;

       public ForecastAdapterViewHolder(View view) {
            super(view);
           text =  view.findViewById(R.id.menu_title);
           text2 =  view.findViewById(R.id.menu_price);
        }

    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     *
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.menu_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     *
     * Vykresluje/nastavuje/vizualizuje data
     *
     */
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {

        String menuTitle = menuData.get(position).get(0);
        forecastAdapterViewHolder.text.setText(menuTitle);

        String menuPrice = menuData.get(position).get(1);
        if(menuPrice.isEmpty())
            forecastAdapterViewHolder.text2.setText("0 €");
        else
            forecastAdapterViewHolder.text2.setText(menuPrice);


    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == menuData) return 0;
        return menuData.size();
    }

    /**
     * This method is used to set the movies on a Adapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param data The new weather data to be displayed.
     */
    public void setMenuData(List<List<String>> data) {
        menuData = data;
        notifyDataSetChanged();
    }
}