package com.eduardo.fabs.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.eduardo.fabs.R;
import com.eduardo.fabs.models.ExpandedMenuModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Eduardo on 01/08/2016.
 */

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private List<ExpandedMenuModel> mListDataHeader; // header titles

    // child data in format of header title, child title
    private HashMap<ExpandedMenuModel, List<String>> mListDataChild;
    ExpandableListView expandList;

    public ExpandableListAdapter(Context context, List<ExpandedMenuModel> listDataHeader, HashMap<ExpandedMenuModel, List<String>> listChildData, ExpandableListView mView) {
        this.mContext = context;
        this.mListDataHeader = listDataHeader;
        this.mListDataChild = listChildData;
        this.expandList = mView;
    }

    @Override
    public int getGroupCount() {
        int i = mListDataHeader.size();
        return this.mListDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childCount;
        try {
            childCount = this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).size();
        } catch (NullPointerException n){
            childCount = 0;
        }
        return childCount;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.mListDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this.mListDataChild.get(this.mListDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
        ExpandedMenuModel headerTitle = (ExpandedMenuModel) getGroup(groupPosition);

        LayoutInflater layoutInflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (groupPosition == 2){
            convertView = layoutInflater.inflate(R.layout.nav_divider, null);
            convertView.setOnClickListener(null);
            return convertView;
        } else if(groupPosition == 1){
            convertView = layoutInflater.inflate(R.layout.nav_submenu_parent, null);
            convertView.setOnClickListener(null);
        } else {
            convertView = layoutInflater.inflate(R.layout.nav_submenu_parent, null);
        }

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.textView_parent);
        ImageView headerIcon = (ImageView) convertView.findViewById(R.id.iconimage);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle.getIconName());
        headerIcon.setImageResource(headerTitle.getIconImg());

        ImageView listHeaderArrow = (ImageView) convertView.findViewById(R.id.collapse);
        if ( getChildrenCount( groupPosition ) == 0 ) {
            listHeaderArrow.setVisibility( View.INVISIBLE );
        } else {
            listHeaderArrow.setVisibility( View.VISIBLE );
            int imageResourceId = isExpanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float;
            listHeaderArrow.setImageResource(imageResourceId);
        }
        listHeaderArrow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                else ((ExpandableListView) parent).expandGroup(groupPosition, true);

            }
        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.nav_submenu_child, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.textView_child);

        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}