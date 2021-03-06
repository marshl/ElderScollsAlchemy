package com.marshl.elderalchemy;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;

public class IngredientListFragment extends Fragment implements DialogInterface.OnClickListener {

    private AlchemyGame game;
    private IngredientListAdapter ingredientListAdapter;

    public static IngredientListFragment newInstance(AlchemyGame game) {
        IngredientListFragment fragment = new IngredientListFragment();

        Bundle args = new Bundle();
        args.putParcelable(AlchemyGame.ALCHEMY_GAME_PARCEL_NAME, game);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.game = this.getArguments().getParcelable(AlchemyGame.ALCHEMY_GAME_PARCEL_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.ingredient_list_fragment, container, false);

        ExpandableListView ingredientListView = rootView.findViewById(R.id.ingredient_list);
        this.ingredientListAdapter = new IngredientListAdapter(this.getActivity(), this.game);
        ingredientListView.setAdapter(this.ingredientListAdapter);

        ingredientListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (ExpandableListView.getPackedPositionType(id) == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                    builder.setMessage("Choose one");
                    builder.setPositiveButton("Select all", IngredientListFragment.this)
                            .setNegativeButton("Select None", IngredientListFragment.this);
                    builder.show();
                    return true;
                }

                return false;
            }
        });

        ingredientListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                IngredientListAdapter adapter = (IngredientListAdapter) parent.getExpandableListAdapter();
                Ingredient ingredient = (Ingredient) adapter.getChild(groupPosition, childPosition);
                ingredient.setSelected(!ingredient.isSelected());

                adapter.notifyDataSetChanged();

                int index = parent.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                parent.setItemChecked(index, true);
                return true;
            }
        });

        return rootView;
    }

    public void refreshIngredients() {
        this.ingredientListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                this.setAllIngredientSelection(true);
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                this.setAllIngredientSelection(false);
                break;
        }
    }

    private void setAllIngredientSelection(boolean selected) {
        this.game.setIngredientSelection(selected);
        this.ingredientListAdapter.notifyDataSetChanged();
    }
}
