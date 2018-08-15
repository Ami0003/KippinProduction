package com.kippin.classification;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kippin.connectedbankacc.CustomSpinner;
import com.kippin.kippin.R;
import com.kippin.superviews.listeners.CommonCallbacks;
import com.kippin.utils.Singleton;
import com.kippin.utils.Url;
import com.kippin.utils.Utility;
import com.kippin.utils.dialogbox.DialogBox;
import com.kippin.utils.dialogbox.DialogBoxListener;
import com.kippin.utils.wheel.commonAdapter.CommonAdapter;
import com.kippin.utils.wheel.commonAdapter.CommonAdapterCallaback;
import com.kippin.webclient.WSInterface;
import com.kippin.webclient.WSMethods;
import com.kippin.webclient.WSUtils;
import com.kippin.webclient.model.ArrayListPost;
import com.kippin.webclient.model.ModalClassificationType;
import com.kippin.webclient.model.ModelCategoryList;
import com.kippin.webclient.model.ModelCategoryLists;
import com.kippin.webclient.model.ModelClassification;
import com.kippin.webclient.model.ModelClassifications;
import com.kippin.webclient.model.TemplateData;
import com.kippin.webclient.model.category.OnCategoryLoaded;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * Created by gaganpreet.singh on 2/5/2016.
 */
public class DialogClassification implements CommonCallbacks, View.OnClickListener,
        AdapterView.OnItemClickListener {

    public ModelClassification selectedClassification;
    Activity context;
    ArrayList<ModelClassification> strings = new ArrayList<ModelClassification>();
    EditText editTextEmail, etDescription, etChartAccNumber;
    TextView tvClassificationType;
    com.kippin.views.CustomSpinner spClassificationType;
    com.kippin.connectedbankacc.CustomSpinner spList;
    Dialog lDialog;
    StickyListHeadersListView lvList;
    boolean isListingOpened = true;
    AdapterNewClassification mAdapterNewClassification;
    String classificationName = "";
    ArrayList<ModelCategoryList> categoryLists;
    AdapterCategory adapterCategory;
    ArrayList<ModalClassificationType> classificationTypes = null;
    Gson gson = new Gson();
    CommonAdapter commonAdapter;
    AdapterClassification adapterClassification;
    Handler handler = new Handler();
    ClassificationCallback classificationCallback;
    private ArrayList<ModelClassification> posts;
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            mAdapterNewClassification.filter(s.toString());

//            strings = new ArrayList<ModelClassification>();
//
//            for (int i = 0; i < posts.size(); i++) {
//                ModelClassification classification = posts.get(i);
//
//                String classification_ = classification.getClassificationType();
//
//                classification_ = classification_.toLowerCase();
//
//                s = s.toString().toLowerCase();
//
//                if (classification_.contains(s.toString()) || classification_.startsWith(s.toString())) {
//                    strings.add(classification);
//                }
//
//            }

//        AdapterClassification adapterClassification = new AdapterClassification(context, strings);
//        lvList.setAdapter(adapterClassification);
//        lvList.setOnItemClickListener(itemClick);

         //   loadList(strings);


        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    private String statementId;
    private Button btnAddClassification;
    private ImageView ivClose;
    private TextView tvHeadingClassification;
    private int selectedClasstficationTypePosition = -1;
    private AdapterView.OnItemClickListener itemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            selectedClassification = strings.get(position);
            classificationCallback.onClassificatonSelected(selectedClassification, position);
            lDialog.dismiss();

           /* AdapterClassification adapterClassification = new AdapterClassification(context, new ArrayList<ModelClassification>());
            lvList.setAdapter(adapterClassification);*/
            loadList(new ArrayList<ModelClassification>());

            editTextEmail.removeTextChangedListener(textWatcher);
            editTextEmail.setText(selectedClassification.getClassificationType());

            strings = new ArrayList<ModelClassification>();
            ModelClassification classification = posts.get(position);
            strings.add(classification);

            editTextEmail.addTextChangedListener(textWatcher);


        }
    };

    public DialogClassification(final Activity context, String statementId) {

        this.context = context;

        initialiseUI();

        setUpListeners();

        ModelCategoryLists modelCategoryLists = ModelCategoryLists.getInstance();

        modelCategoryLists.loadData(context, new OnCategoryLoaded() {
            @Override
            public void onCategoryLoaded(ArrayList<ModelCategoryList> modelCategoryLists) {
                categoryLists = modelCategoryLists;

                inflateCategories();
            }
        });



    }

    public void inflateCategories() {
        adapterCategory = new AdapterCategory(context, categoryLists);
        spList.setAdapter(adapterCategory);
    }

    public void requestClassifications(final WSInterface wsInterface) {
        Log.e("URL:", "" + Url.New_URL_CLASSIFICATIONS + File.separator + Singleton.getUser().getId());
        WSUtils.hitServiceGet(context, Url.New_URL_CLASSIFICATIONS + File.separator + Singleton.getUser().getId(), new ArrayListPost(), ModelClassifications.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
                onClassificationGet(data);
                if (wsInterface != null)
                    wsInterface.onResult(requestCode, data);
            }
        });

    }

    private void onClassificationTypeClick() {
        if (classificationTypes == null) {
            loadClassificationType();
        } else {
            spClassificationType.performClick();
        }

    }

    private void loadClassificationType() {

        WSUtils.hitServiceGet(context, Url.URL_CLASSIFICATION_LIST, new ArrayListPost(), TemplateData.class, new WSInterface() {
            @Override
            public void onResult(int requestCode, TemplateData data) {
                Type listType = new TypeToken<List<ModalClassificationType>>() {
                }.getType();
                classificationTypes = gson.fromJson(data.data, listType);

                ArrayList<String> strings = new ArrayList<String>();

                for (int i = 0; i < classificationTypes.size(); i++) {
                    strings.add(classificationTypes.get(i).getClassificationType());
                }

                commonAdapter = new CommonAdapter(context, strings, new CommonAdapterCallaback() {
                    @Override
                    public void onClick(int position) {

                        DialogClassification.this.selectedClasstficationTypePosition = position;


                        tvClassificationType.setText(classificationTypes.get(position).getClassificationType());


                        spClassificationType.onDetachedFromWindow();

                    }
                });

                spClassificationType.setAdapter(commonAdapter);

                Utility.showProgressDialog(context, R.string.pls_wait);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Utility.dismissProgressDialog();
                                spClassificationType.performClick();
                                spClassificationType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        selectedClasstficationTypePosition = position;
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {
                                    }
                                });
                            }
                        });

                    }
                }).start();


            }
        });

    }

    private void onClassificationGet(TemplateData data) {
        ModelClassifications modelClassifications = data.getData(ModelClassifications.class);

        posts = Utility.getSortedClassifications(modelClassifications.modelClassifications);
        strings = posts;
        loadList(posts);

    }

    public void show(final ClassificationCallback callback) {
        this.classificationCallback = callback;
        strings = posts;
        editTextEmail.addTextChangedListener(textWatcher);
        lDialog.show();
        isListingOpened = true;
        editTextEmail.addTextChangedListener(textWatcher);
        editTextEmail.setText(editTextEmail.getText().toString());
    }

    private void loadList(ArrayList<ModelClassification> modelClassifications) {
//        AdapterClassification adapterClassification = new AdapterClassification(context, modelClassifications);
//        lvList.setAdapter(adapterClassification);
//        lvList.setOnItemClickListener(itemClick);

        if(modelClassifications.size() < 0)
            return;

        mAdapterNewClassification = new AdapterNewClassification(context, modelClassifications);
        lvList.setAdapter(mAdapterNewClassification);
        lvList.setOnItemClickListener(this);
    }


    @Override
    public void initialiseUI() {
        lDialog = new Dialog(context);
        lDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        lDialog.setCancelable(false);
        lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lDialog.setContentView(R.layout.dialog_classification);


        editTextEmail = (EditText) lDialog.findViewById(R.id.tvEnterClassification);
        etDescription = (EditText) lDialog.findViewById(R.id.tvEnterDesc);
        etChartAccNumber = (EditText) lDialog.findViewById(R.id.etChartAccNumber);

        tvClassificationType = (TextView) lDialog.findViewById(R.id.tvClassificationType);

        spClassificationType = (com.kippin.views.CustomSpinner) lDialog.findViewById(R.id.spClassificationType);

        ivClose = (ImageView) lDialog.findViewById(R.id.ivClose);
        spList = (CustomSpinner) lDialog.findViewById(R.id.spEnterCategory);
        btnAddClassification = (Button) lDialog.findViewById(R.id.btnAddClassification);
        //lvList = (ListView)lDialog.findViewById(R.id.lvClassifications);
        lvList = (StickyListHeadersListView) lDialog.findViewById(R.id.lvClassifications);
        tvHeadingClassification = (TextView) lDialog.findViewById(R.id.tvHeadingClassification);
    }

    @Override
    public void setUpListeners() {
        editTextEmail.setOnClickListener(this);
        ivClose.setOnClickListener(this);
        tvClassificationType.setOnClickListener(this);
        btnAddClassification.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.tvEnterClassification) {
            editTextEmail.removeTextChangedListener(textWatcher);
            editTextEmail.setText("");
            editTextEmail.addTextChangedListener(textWatcher);
        } else if (id == R.id.ivClose) {
            closeDialog();

        } else if (id == R.id.btnAddClassification) {
            if (isListingOpened)
                switchView();
            else addClassification();
        } else if (id == R.id.tvClassificationType) {
            onClassificationTypeClick();
        }

        /*switch (v.getId()){

            case R.id.tvEnterClassification:
                editTextEmail.removeTextChangedListener(textWatcher);
                editTextEmail.setText("");
                editTextEmail.addTextChangedListener(textWatcher);
            break;

            case R.id.ivClose:
                closeDialog();
                break;

            case R.id.btnAddClassification:
                if(isListingOpened)
                switchView();
                else addClassification();

                break;


            case R.id.tvClassificationType:

                onClassificationTypeClick();

                break;

        }*/

    }


    private void closeDialog() {

        if (isListingOpened) {
            lDialog.dismiss();
        } else {
            switchView();
        }

    }

    private void switchView() {

        int heading_id = 0;

        View[] listing = {lvList};
        View[] addClassification = {etDescription, spList, tvClassificationType, etChartAccNumber};

        String text_classification = "";

        if (isListingOpened) {
            editTextEmail.removeTextChangedListener(textWatcher);
//            now classification add view need to open
            classificationName = editTextEmail.getText().toString();
            heading_id = R.string.add_classification;
            text_classification = "";
            Utility.setVisibility(View.GONE, listing);
            btnAddClassification.setText("ADD");
            Utility.removeText(listing);
            Utility.setVisibility(View.VISIBLE, addClassification);
        } else {
//          now classification listing view need to open
            editTextEmail.addTextChangedListener(textWatcher);
            heading_id = R.string.classification;
            btnAddClassification.setText("ADD NEW CLASSIFICATION");
            text_classification = classificationName;
            Utility.setVisibility(View.GONE, addClassification);
            Utility.setVisibility(View.VISIBLE, listing);
            Utility.removeText(addClassification);
            spList.setSelection(0);

        }


        editTextEmail.setText(text_classification);
        tvHeadingClassification.setText(heading_id);
        isListingOpened = !isListingOpened;
    }


    private void addClassification() {

        if (editTextEmail.getText().toString().length() == 0) {
            new DialogBox(context, "Please Enter Classification name", (DialogBoxListener) null);
            return;
        }

        if (spList.getCount() == 0 || spList.getSelectedItemPosition() == 0) {
            new DialogBox(context, "Please Enter Category", (DialogBoxListener) null);
            return;
        }

        if (classificationTypes == null || selectedClasstficationTypePosition == -1) {
            new DialogBox(context, "Please Enter Classification Type", (DialogBoxListener) null);
            return;
        }

        if (etChartAccNumber.getText().toString().length() == 0) {
            new DialogBox(context, "Please Enter Chart Account Number", (DialogBoxListener) null);
            return;
        }


        if (etChartAccNumber.getText().toString().length() == 0) {
            new DialogBox(context, "Please Enter Chart Account Number", (DialogBoxListener) null);
            return;
        }
//        {"CategoryId":"2","ctext":"hdsefre","UserID":"6","cDesc":"dasdasd"}
        final ArrayListPost posts_data = new ArrayListPost();
        posts_data.add("CategoryId", categoryLists.get(spList.getSelectedItemPosition()).getId());
        posts_data.add("ctext", editTextEmail.getText().toString());
        posts_data.add("UserID", Singleton.getUser().getId());
        posts_data.add("cDesc", etDescription.getText().toString());
        posts_data.add("ClassificationTypeId", classificationTypes.get(selectedClasstficationTypePosition).getId());
        posts_data.add("ChartAccountNumber", etChartAccNumber.getText().toString());
        WSUtils.hitService(context, Url.URL_SAVE_CLASSIFICATION, false, posts_data, WSMethods.POST, ModelClassifications.class, new WSInterface() {

            @Override
            public void onResult(int requestCode, final TemplateData data) {

                ModelClassifications modelClassifications = data.getData(ModelClassifications.class);


                if (modelClassifications.modelClassifications.size() == 1
                        && !modelClassifications.modelClassifications.get(0).getResponseCode().equalsIgnoreCase("1")) {
                    new DialogBox(context, modelClassifications.modelClassifications.get(0).getResponseMessage(), (DialogBoxListener) null);

                } else {

//                    modelClassifications.modelClassifications = modelClassifications.getSortedClassifications();


                    modelClassifications.modelClassifications = Utility.getSortedClassifications(modelClassifications.modelClassifications);

                    new DialogBox(context, "Added Successfully", new DialogBoxListener() {
                        @Override
                        public void onDialogOkPressed() {

                            String selection = editTextEmail.getText().toString();

                            onClassificationGet(data);

                            for (int i = 0; i < posts.size(); i++) {

                                if (posts.get(i).getClassificationType().toLowerCase().equalsIgnoreCase(selection)) {
                                    classificationName = (posts.get(i).getClassificationType());
                                    switchView();

                                    selectedClassification = posts.get(i);
                                    classificationCallback.onClassificatonSelected(selectedClassification, i);
                                    lDialog.dismiss();



                                   /* AdapterClassification adapterClassification = new AdapterClassification(context, new ArrayList<ModelClassification>());
                                    lvList.setAdapter(adapterClassification);*/
                                    loadList(new ArrayList<ModelClassification>());

                                    editTextEmail.removeTextChangedListener(textWatcher);
                                    editTextEmail.setText(selectedClassification.getClassificationType());

                                    strings = new ArrayList<ModelClassification>();
                                    ModelClassification classification = posts.get(i);
                                    strings.add(classification);

                                    editTextEmail.addTextChangedListener(textWatcher);

                                    DialogClassification.this.selectedClasstficationTypePosition = -1;

                                    break;
                                }
                            }
                        }
                    });


                }
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        selectedClassification = strings.get(position);
        classificationCallback.onClassificatonSelected(selectedClassification, position);
        lDialog.dismiss();

           /* AdapterClassification adapterClassification = new AdapterClassification(context, new ArrayList<ModelClassification>());
            lvList.setAdapter(adapterClassification);*/
    }
}
