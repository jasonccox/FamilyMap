package com.jasoncarloscox.familymap.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jasoncarloscox.familymap.R;
import com.jasoncarloscox.familymap.model.Event;
import com.jasoncarloscox.familymap.model.Person;
import com.jasoncarloscox.familymap.util.ResourceGenerator;

import java.util.List;

/**
 * Adapter to display a search result (Person or Event) in the RecyclerView on
 * the PersonActivity
 */
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private List<Object> results;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private static final int ICON_SIZE_DP = 20;

        private View itemView;
        private ImageView imgIcon;
        private TextView txtTitle;
        private TextView txtSubtitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;

            imgIcon = itemView.findViewById(R.id.detail_item_icon);
            txtTitle = itemView.findViewById(R.id.detail_item_title);
            txtSubtitle = itemView.findViewById(R.id.detail_item_subtitle);
        }

        public void bindData(final Object result, boolean altBackground) {
            int horizontalPad = (int) dpToPx(20);
            int verticalPad = (int) dpToPx(10);
            itemView.setPadding(horizontalPad, verticalPad, horizontalPad, verticalPad);

            if (altBackground) {
                itemView.setBackgroundResource(R.color.colorBackgroundDark);
            } else {
                itemView.setBackgroundResource(R.color.colorBackground);
            }

            if (result instanceof Event) {
                bindEvent((Event) result);
            } else if (result instanceof Person) {
                bindPerson((Person) result);
            }
        }

        private void bindEvent(final Event event) {
            imgIcon.setImageDrawable(ResourceGenerator.eventIcon(
                    itemView.getContext(),
                    ICON_SIZE_DP,
                    R.color.colorAccent));

            txtTitle.setText(itemView.getResources().getString(
                    R.string.event_result_title,
                    event.getPerson().getFullName(),
                    event.getType()));

            txtSubtitle.setText(itemView.getResources().getString(
                    R.string.event_date_and_location,
                    event.getYear(),
                    event.getCity(),
                    event.getCountry()));

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),
                            EventActivity.class);
                    intent.putExtra(EventActivity.KEY_EVENT_ID, event.getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        private void bindPerson(final Person person) {
            imgIcon.setImageDrawable(ResourceGenerator.genderIcon(
                    itemView.getContext(),
                    person.getGender(),
                    ICON_SIZE_DP));

            txtTitle.setText(person.getFullName());

            txtSubtitle.setText(ResourceGenerator.genderString(
                    person.getGender(),
                    itemView.getResources()));

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(),
                            PersonActivity.class);
                    intent.putExtra(PersonActivity.KEY_PERSON_ID, person.getId());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        /**
         * @param dp a size in dp
         * @return an equivalent size in pixels
         */
        private float dpToPx(float dp) {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                    itemView.getResources().getDisplayMetrics());
        }
    }

    public SearchResultAdapter(List<Object> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        boolean altBackground = (i % 2 == 0);
        viewHolder.bindData(results.get(i), altBackground);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.itemview_details;
    }
}
