package com.kippinretail.KippinInvoice;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.kippinretail.ApplicationuUlity.CommonUtility;
import com.kippinretail.ApplicationuUlity.Log;
import com.kippinretail.CommonDialog.CommonDialog;
import com.kippinretail.KippinInvoice.InvoiceAdapter.GoodsAdapter;
import com.kippinretail.KippinInvoice.ModalInvoice.AddNewItem;
import com.kippinretail.KippinInvoice.ModalInvoice.Revenue;
import com.kippinretail.R;
import com.kippinretail.SuperActivity;
import com.kippinretail.loadingindicator.LoadingBox;
import com.kippinretail.retrofit.RestClient;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.kippinretail.KippinInvoice.CommonNumberFormatter.getRound;
import static com.kippinretail.KippinInvoice.CommonNumberFormatter.getRoundSimple;
import static java.lang.Float.parseFloat;

/**
 * Created by Codewix Dev on 7/27/2017.
 */

public class ItemDetailRejected extends SuperActivity implements View.OnClickListener {
    Button btn_save;
    EditText etItemNumber, etDescription, etQuantityHours, etRate, etDiscountAmount, etDiscountPercentage;
    TextView tvAmountRate, tvServiceProductType;
    Spinner spinnerProductTyoe;
    EditText etTax, etTaxPercentage, etHST, etGST, etQST, etPST;
    EditText etHSTPercentage, etGSTPercentage, etQSTPercentage, etPSTPercentage;
    LinearLayout llTaxTypeLayout;
    TextView tvSubTotal;
    RelativeLayout taxLayout;
    AddNewItem itemModal1;
    float fDiscount = 0, fTax, fGST, fQST, fHST, fPST, fpGST, fpQST, fpHST, fpPST, fpTax, fpDiscount;
    double dAmount = 0;
    private List<Revenue> revenueList;
    private GoodsAdapter goodsAdapter;
    private AddNewItem itemModal;
    private String DataDetails;
    public static int servicetypeValue = 0;
    private String position;
    private String mRevenueID = "";
    private String mProductType = "";
    private String finance;
    private int itemPosition;
    private RelativeLayout rlSpinnerLayout;
    private float totalAmount;

    float dis_Amount = 0;
    TextWatcher TAXWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            float disAmount = 0;
            if (etRate.getText().length() != 0) {
                if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
                    if (etTaxPercentage.getText().toString().length() == 0) {
                        if (s.length() != 0) {
                            if (etDiscountAmount.getText().length() != 0) {
                                disAmount = Float.parseFloat(tvAmountRate.getText().toString()) + Float.parseFloat(s.toString()) - Float.parseFloat(etDiscountAmount.getText().toString());
                                if (disAmount > 0) {
                                    tvSubTotal.setText("" + getRoundSimple(disAmount));
                                    dis_Amount = Float.parseFloat(getRoundSimple(disAmount));
                                } else {
                                    tvSubTotal.setText("" + disAmount);
                                    dis_Amount = Float.parseFloat(getRoundSimple(disAmount));
                                }
                            } else {
                                disAmount = Float.parseFloat(tvAmountRate.getText().toString()) + Float.parseFloat(s.toString());
                                tvSubTotal.setText("" + getRoundSimple(disAmount));
                                dis_Amount = Float.parseFloat(getRoundSimple(disAmount));
                            }
                        } else {
                            //tvSubTotal.setText("" + totalAmount);
                            tvSubTotal.setText("" + getAmountResult());
                        }
                    } else {
                        tvSubTotal.setText("" + getAmountResult());
                        removeListnerWithErrorMessage(etTax, getResources().getString(R.string.tax_amount_percentage), this);
                    }
                } else {
                    if (etDiscountAmount.getText().toString().length() != 0) {
                        tvSubTotal.setText("" + (Float.parseFloat(tvAmountRate.getText().toString()) - Float.parseFloat(etDiscountAmount.getText().toString())));

                    }
                    tvSubTotal.setText("" + getAmountResult());
                    removeListnerWithErrorMessage(etTax, getResources().getString(R.string.enter_discount_amount), this);
                }
            } else {
                tvSubTotal.setText("" + getAmountResult());
                removeListnerWithErrorMessage(etTax, getResources().getString(R.string.quantity_rate_error), this);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher TAXPercentageWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            float disAmount = 0;
            if (etRate.getText().length() != 0) {
                if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
                    if (etTax.getText().toString().length() == 0) {
                        if (s.length() != 0) {
                            if (etDiscountPercentage.getText().length() != 0) {
                                tvSubTotal.setText(calculateTaxPercentage(s.toString()));
                            } else {
                                tvSubTotal.setText(calculateTaxPercentage(s.toString()));
                            }

                        } else {
                            tvSubTotal.setText("" + totalAmount);
                        }

                    } else {
                        tvSubTotal.setText("" + getAmountResult());
                        removeListnerWithErrorMessage(etTaxPercentage, getResources().getString(R.string.tax_amount_percentage), this);

                    }
                } else {
                    tvSubTotal.setText("" + getAmountResult());
                    removeListnerWithErrorMessage(etTaxPercentage, getResources().getString(R.string.enter_discount_amount), this);
                }
            } else {
                tvSubTotal.setText("" + getAmountResult());
                removeListnerWithErrorMessage(etTaxPercentage, getResources().getString(R.string.quantity_rate_error), this);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher HSTPercentageWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //  if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
           /* if (etHST.getText().toString().length() == 0) {
                tvSubTotal.setText("" + getAmountResult());
            } else {
                removeListnerWithErrorMessage(etHSTPercentage, getResources().getString(R.string.tax_amount_percentage), this);
            }*/


            if (etHST.getText().toString().length() == 0) {
                if (s.length() != 0) {
                    //totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(s.toString());
                    // totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        //  totalAmount = Float.parseFloat(calculate_Tax_Percentage());
                        float perc = Float.parseFloat(calculate_Tax_Percentage());
                        // float total=Float.parseFloat(calculationTotalRateAndQuantity());
                        totalAmount = perc;
                    } else {
                        totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    }
                    tvSubTotal.setText("" + getAmountResult());
                } else {
                    // totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        totalAmount = Float.parseFloat(calculate_Tax_Percentage());
                    } else {
                        totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    }
                    tvSubTotal.setText("" + getAmountResult());
                }


            } else {
                removeListnerWithErrorMessage(etHSTPercentage, getResources().getString(R.string.tax_amount_percentage), this);
            }

            //} else {
            //  removeListnerWithErrorMessage(etHSTPercentage, getResources().getString(R.string.enter_discount_amount), this);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    // GST percentage
    TextWatcher GSTPercentageWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //  if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
            /*if (etGST.getText().toString().length() == 0) {
                tvSubTotal.setText("" + getAmountResult());
            } else {
                removeListnerWithErrorMessage(etGSTPercentage, getResources().getString(R.string.tax_amount_percentage), this);
            }*/


            if (etGST.getText().toString().length() == 0) {
                if (s.length() != 0) {
                    //  totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(s.toString());
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        float perc = Float.parseFloat(calculate_Tax_Percentage());
                        //float total=Float.parseFloat(calculationTotalRateAndQuantity());
                        totalAmount = perc;
                        //totalAmount = Float.parseFloat(calculate_Tax_Percentage());
                    } else {
                        totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    }

                    tvSubTotal.setText("" + getAmountResult());
                } else {
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        totalAmount = Float.parseFloat(calculate_Tax_Percentage());
                    } else {
                        totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    }
                    //totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    tvSubTotal.setText("" + getAmountResult());
                }
            } else {
                removeListnerWithErrorMessage(etGSTPercentage, getResources().getString(R.string.tax_amount_percentage), this);
            }

            //} else {
            //  removeListnerWithErrorMessage(etGSTPercentage, getResources().getString(R.string.enter_discount_amount), this);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    // QST Percenage
    TextWatcher QSTPercentageWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
            /*if (etQST.getText().toString().length() == 0) {
                tvSubTotal.setText("" + getAmountResult());
            } else {
                removeListnerWithErrorMessage(etQSTPercentage, getResources().getString(R.string.tax_amount_percentage), this);
            }*/


            if (etQST.getText().toString().length() == 0) {
                if (s.length() != 0) {
                    //totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(s.toString());
                    //totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        float perc = Float.parseFloat(calculate_Tax_Percentage());
                        // float total=Float.parseFloat(calculationTotalRateAndQuantity());
                        totalAmount = perc;
                        // totalAmount = Float.parseFloat(calculate_Tax_Percentage());
                    } else {
                        totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    }
                    tvSubTotal.setText("" + getAmountResult());
                } else {
                    // totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        totalAmount = Float.parseFloat(calculate_Tax_Percentage());
                    } else {
                        totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    }
                    tvSubTotal.setText("" + getAmountResult());
                }
            } else {
                removeListnerWithErrorMessage(etQSTPercentage, getResources().getString(R.string.tax_amount_percentage), this);
            }
            //} else {
            //  removeListnerWithErrorMessage(etQSTPercentage, getResources().getString(R.string.enter_discount_amount), this);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private String calculate_Tax_Percentage() {
        float value = 0;
        if (etDiscountPercentage.getText().toString().length() != 0) {
            float percentage = Float.parseFloat(tvAmountRate.getText().toString()) / 100 * parseFloat(etDiscountPercentage.getText().toString());
            value = Float.parseFloat(tvAmountRate.getText().toString()) - percentage;
        } else {
            if (etDiscountAmount.getText().toString().length() != 0 && tvAmountRate.getText().toString().length() != 0) {
                value = Float.parseFloat(tvAmountRate.getText().toString()) - Float.parseFloat(etDiscountAmount.getText().toString());
            }

        }
        Log.e("getRoundSimple(value):", "" + getRoundSimple(value));

        return getRoundSimple(value);

    }

    // QST Percenage
    TextWatcher PSTPercentageWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
            /*if (etPST.getText().toString().length() == 0) {
                tvSubTotal.setText("" + getAmountResult());
            } else {
                removeListnerWithErrorMessage(etPSTPercentage, getResources().getString(R.string.tax_amount_percentage), this);
            }*/


            if (etPST.getText().toString().length() == 0) {
                if (s.length() != 0) {
                    //totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(s.toString());
                    // totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        float perc = Float.parseFloat(calculate_Tax_Percentage());
                        //  float total=Float.parseFloat(calculationTotalRateAndQuantity());
                        totalAmount = perc;
                    } else {
                        totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    }
                    tvSubTotal.setText("" + getAmountResult());
                } else {
                    //totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        totalAmount = Float.parseFloat(calculate_Tax_Percentage());
                    } else {
                        totalAmount = Float.parseFloat(calculationTotalRateAndQuantity());
                    }
                    tvSubTotal.setText("" + getAmountResult());
                }
            } else {
                removeListnerWithErrorMessage(etPSTPercentage, getResources().getString(R.string.tax_amount_percentage), this);
            }

            //} else {
            //  removeListnerWithErrorMessage(etPSTPercentage, getResources().getString(R.string.enter_discount_amount), this);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher watcherTaxAmount = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
           /* if (s.length() == 0 || s.equals(".")) {
                s = "0";
            }*/
            if (s.length() != 0) {
                if (etRate.getText().length() != 0 && !etRate.getText().toString().equals("")) {
                    if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
                        if (etDiscountPercentage.getText().toString().length() == 0 || etDiscountAmount.getText().length() == 0) {
                            if (!tvAmountRate.getText().toString().equals("") || s.length() != 0 || !s.equals(".")) {
                                if (Float.parseFloat(s.toString()) < Float.parseFloat(tvAmountRate.getText().toString())) {
                                    if (s.length() != 0) {

                                        totalAmount = Float.parseFloat(tvAmountRate.getText().toString()) - Float.parseFloat(s.toString());
                                    } else {
                                        totalAmount = 0;
                                    }
                                    tvSubTotal.setText("" + getAmountResult());
                                } else {
                                    tvSubTotal.setText("");
                                    removeListnerWithErrorMessage(etDiscountAmount, getResources().getString(R.string.discount_percentage_must_be_less), this);
                                }
                                if (etTaxPercentage.getText().length() != 0) {
                                    float percentageValue1 = (Float.parseFloat(tvSubTotal.getText().toString()) / 100) * Float.parseFloat(etTaxPercentage.getText().toString());
                                    totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + percentageValue1;
                                    tvSubTotal.setText("" + totalAmount);
                                }
                                if (etTax.getText().toString().length() != 0) {
                                    tvSubTotal.setText("" + getRoundSimple(Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(etTax.getText().toString())));
                                }
                            } else {
                                tvSubTotal.setText("" + getAmountResult());
                                removeListnerWithErrorMessage(etDiscountAmount, getResources().getString(R.string.discount_percentage_must_be_less), this);
                            }
                        } else {
                            tvSubTotal.setText("" + getAmountResult());
                            removeListnerWithErrorMessage(etDiscountAmount, getResources().getString(R.string.tax_amount_percentage), this);
                        }

                    } else {
                        tvSubTotal.setText(tvAmountRate.getText().toString());
                        if (etTaxPercentage.getText().length() != 0) {
                            float percentageValue1 = (Float.parseFloat(tvSubTotal.getText().toString()) / 100) * Float.parseFloat(etTaxPercentage.getText().toString());
                            totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + percentageValue1;
                            tvSubTotal.setText("" + totalAmount);
                        }
                        if (etTax.getText().toString().length() != 0) {
                            tvSubTotal.setText("" + getRoundSimple(Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(etTax.getText().toString())));
                        }
                        tvSubTotal.setText("" + getAmountResult());
                        removeListnerWithErrorMessage(etDiscountAmount, getResources().getString(R.string.enter_discount_amount), this);
                    }
                } else {
                    tvSubTotal.setText("" + getAmountResult());
                    removeListnerWithErrorMessage(etDiscountAmount, getResources().getString(R.string.quantity_rate_error), this);
                }
            } else {
                totalAmount = 0;
                tvSubTotal.setText("" + getAmountResult());
                removeListnerWithErrorMessage(etDiscountAmount, getResources().getString(R.string.quantity_rate_error), this);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher watcherTaxPercentage = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            /*if (s.length() == 0) {
                s = "0";
            }*/
            if (s.length() != 0) {
                if (etRate.getText().length() != 0) {
                    if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
                        if (etDiscountAmount.getText().toString().length() == 0) {
                            if (Float.parseFloat(s.toString()) < 100) {
                                if (s.length() != 0) {

                                    float percentageValue = (Float.parseFloat(tvAmountRate.getText().toString()) / 100) * Float.parseFloat(s.toString());
                                    totalAmount = Float.parseFloat(tvAmountRate.getText().toString()) - percentageValue;
                                    if (CommonUtility.IsFinance.equals("true")) {
                                        tvSubTotal.setText("" + getAmountResult());
                                    } else {
                                        //  totalAmount = totalAmount + Float.parseFloat(s.toString());
                                        tvSubTotal.setText("" + totalAmount);
                                    }

                                    if (etTaxPercentage.getText().length() != 0) {
                                        float percentageValue1 = (Float.parseFloat(tvSubTotal.getText().toString()) / 100) * Float.parseFloat(etTaxPercentage.getText().toString());
                                        totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + percentageValue1;
                                        tvSubTotal.setText("" + getRoundSimple(totalAmount));
                                    }
                                    if (etTax.getText().toString().length() != 0) {
                                        tvSubTotal.setText("" + getRoundSimple(Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(etTax.getText().toString())));
                                    }

                                }
                            } else {
                                tvSubTotal.setText("" + getAmountResult());
                                removeListnerWithErrorMessage(etDiscountPercentage, getResources().getString(R.string.discount_percentage_must_be_less_100), this);
                            }

                        } else {
                            tvSubTotal.setText("" + getAmountResult());
                            removeListnerWithErrorMessage(etDiscountPercentage, getResources().getString(R.string.tax_amount_percentage), this);

                        }
                    } else {
                        tvSubTotal.setText("" + tvAmountRate.getText().toString());
                        if (etTaxPercentage.getText().length() != 0) {
                            float percentageValue1 = (Float.parseFloat(tvSubTotal.getText().toString()) / 100) * Float.parseFloat(etTaxPercentage.getText().toString());
                            totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + percentageValue1;
                            Log.e("totalAmount:", "" + totalAmount);
                            tvSubTotal.setText("" + getRoundSimple(totalAmount));
                        }
                        if (etTax.getText().toString().length() != 0) {
                            tvSubTotal.setText("" + getRoundSimple(Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(etTax.getText().toString())));
                        }
                        removeListnerWithErrorMessage(etDiscountPercentage, getResources().getString(R.string.enter_discount_amount), this);
                    }
                } else {
                    tvSubTotal.setText("" + getAmountResult());
                    removeListnerWithErrorMessage(etDiscountPercentage, getResources().getString(R.string.quantity_rate_error), this);
                }
            } else {
                totalAmount = 0;
                tvSubTotal.setText("" + getAmountResult());
                removeListnerWithErrorMessage(etDiscountPercentage, getResources().getString(R.string.quantity_rate_error), this);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher HSTWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
           /* if (etHSTPercentage.getText().toString().length() == 0) {
                tvSubTotal.setText("" + getAmountResult());
            } else {
                removeListnerWithErrorMessage(etHST, getResources().getString(R.string.tax_amount_percentage), this);
            }*/
            if (s.length() != 0) {
                if (etHSTPercentage.getText().toString().length() == 0) {
                    tvSubTotal.setText("" + getAmountResult());
                } else {
                    tvSubTotal.setText("" + getAmountResult());
                    removeListnerWithErrorMessage(etHST, getResources().getString(R.string.tax_amount_percentage), this);
                }
            } else {
                tvSubTotal.setText("" + getAmountResult());
                // removeListnerWithErrorMessage(etHST, getResources().getString(R.string.tax_amount_percentage), this);
            }

            //} else {
            //  removeListnerWithErrorMessage(etHST, getResources().getString(R.string.enter_discount_amount), this);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    // Percemtage watcher
    // TaxAmount Watcher
    TextWatcher GSTWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
            if (s.length() != 0) {
                if (etGSTPercentage.getText().toString().length() == 0) {
                    tvSubTotal.setText("" + getAmountResult());
                } else {
                    tvSubTotal.setText("" + getAmountResult());
                    removeListnerWithErrorMessage(etGST, getResources().getString(R.string.tax_amount_percentage), this);
                }
            } else {
                tvSubTotal.setText("" + getAmountResult());
            }



            /*if (etGSTPercentage.getText().toString().length() == 0) {
                tvSubTotal.setText("" + getAmountResult());
            } else {
                removeListnerWithErrorMessage(etGST, getResources().getString(R.string.tax_amount_percentage), this);
            }*/
            //} else {
            //  removeListnerWithErrorMessage(etGST, getResources().getString(R.string.enter_discount_amount), this);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher QSTWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //  if (etDiscountPercentage.getText().toString().length() + etDiscountAmount.getText().toString().length() > 0) {
           /* if (etQSTPercentage.getText().toString().length() == 0) {
                tvSubTotal.setText("" + getAmountResult());
            } else {
                removeListnerWithErrorMessage(etQST, getResources().getString(R.string.tax_amount_percentage), this);
            }*/

            if (s.length() != 0) {
                if (etQSTPercentage.getText().toString().length() == 0) {
                    tvSubTotal.setText("" + getAmountResult());
                } else {
                    tvSubTotal.setText("" + getAmountResult());
                    removeListnerWithErrorMessage(etQST, getResources().getString(R.string.tax_amount_percentage), this);
                }
            } else {
                tvSubTotal.setText("" + getAmountResult());
            }
            //} else {
            //  removeListnerWithErrorMessage(etQST, getResources().getString(R.string.enter_discount_amount), this);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    TextWatcher PSTWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() != 0) {
                if (etPSTPercentage.getText().toString().length() == 0) {
                    tvSubTotal.setText("" + getAmountResult());
                } else {
                    tvSubTotal.setText("" + getAmountResult());
                    removeListnerWithErrorMessage(etPST, getResources().getString(R.string.tax_amount_percentage), this);
                }
            } else {
                tvSubTotal.setText("" + getAmountResult());
            }


            /*if (etPSTPercentage.getText().toString().length() == 0) {
                tvSubTotal.setText("" + getAmountResult());
            } else {
                removeListnerWithErrorMessage(etPST, getResources().getString(R.string.tax_amount_percentage), this);
            }*/
            //} else {
            //  removeListnerWithErrorMessage(etPST, getResources().getString(R.string.enter_discount_amount), this);
            //}
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private boolean isProductSelected = false;
    private float subTotalCost;

    private String calculateTaxPercentage(String s) {
        float value = 0;
        if (etDiscountPercentage.getText().toString().length() != 0) {
            float percentage = Float.parseFloat(tvAmountRate.getText().toString()) / 100 * parseFloat(etDiscountPercentage.getText().toString());
            value = Float.parseFloat(tvAmountRate.getText().toString()) - percentage;
        } else {
            value = Float.parseFloat(tvAmountRate.getText().toString()) - Float.parseFloat(etDiscountAmount.getText().toString());
        }

        float disAmount = (value / 100) * Float.parseFloat(s.toString());
        float percentageValue = value + disAmount;
        return getRoundSimple(percentageValue);

    }

    private String calculationTotalRateAndQuantity() {
        float rate = 0;
        float hours = 0;
        String value = "";
        if (etQuantityHours.getText().toString().length() != 0 && etRate.getText().toString().length() != 0) {
            hours = Float.parseFloat(etQuantityHours.getText().toString());
            rate = Float.parseFloat(etRate.getText().toString());
            float total = hours * rate;
            value = getRoundSimple(total);
            Log.e("value:", "" + value);

        }

        return value;
    }

    private String getAmountResult() {
      /*  Log.e("total-------Amount:", "" + totalAmount);
        double totalAmount_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + totalAmount), 2);
        totalAmount = (float) totalAmount_double;
        String HSTT = "0", GSTT = "0", QSTT = "0", PSTT = "0";
        if (etHST.getText().length() == 0) {
            HSTT = "0";
        } else {
            HSTT = etHST.getText().toString();
            // Log.e("HSTT:", "" + HSTT);
        }
        if (etGST.getText().length() == 0) {
            GSTT = "0";
        } else {
            GSTT = etGST.getText().toString();
            //Log.e("GSTT:", "" + GSTT);
        }
        if (etQST.getText().length() == 0) {
            QSTT = "0";
        } else {
            QSTT = etQST.getText().toString();
            // Log.e("QSTT:", "" + QSTT);
        }
        if (etPST.getText().length() == 0) {
            PSTT = "0";
        } else {
            PSTT = etPST.getText().toString();
            // Log.e("PSTT:", "" + PSTT);
        }
        if (etHSTPercentage.getText().length() != 0) {
            HSTT = "" + totalAmount / 100 * Float.parseFloat(etHSTPercentage.getText().toString());
            double etHSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(HSTT), 2);
            HSTT = "" + etHSTPercentage_double;
        }
        if (etGSTPercentage.getText().length() != 0) {
            Log.e("totalAmount:", "" + totalAmount);
            GSTT = "" + totalAmount / 100 * Float.parseFloat(etGSTPercentage.getText().toString());
            Log.e("GSTT before:", "" + GSTT);
            double etGSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(GSTT), 2);
            GSTT = "" + etGSTPercentage_double;
        }
        if (etQSTPercentage.getText().length() != 0) {
            QSTT = "" + totalAmount / 100 * Float.parseFloat(etQSTPercentage.getText().toString());
            double etQSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(QSTT), 2);
            QSTT = "" + etQSTPercentage_double;
        }
        if (etPSTPercentage.getText().length() != 0) {
            PSTT = "" + totalAmount / 100 * Float.parseFloat(etPSTPercentage.getText().toString());
            double etPSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(PSTT), 2);
            PSTT = "" + etPSTPercentage_double;
        }
        Log.e("PSTT:", "" + Float.parseFloat(PSTT));
        Log.e("totalAmount:", "" + totalAmount);
        Log.e("HSTT:", "" + Float.parseFloat(HSTT));
        Log.e("QSTT:", "" + Float.parseFloat(QSTT));
        Log.e("GSTT:", "" + Float.parseFloat(GSTT));
        Log.e("dis_Amount:", "" + dis_Amount);
        return getRoundSimple(Float.parseFloat(HSTT) + Float.parseFloat(GSTT) +
                Float.parseFloat(QSTT) + Float.parseFloat(PSTT) + totalAmount - dis_Amount);*/


        float mainTotal = Float.parseFloat(calculationTotalRateAndQuantity());
        Log.e("totalAmount:", "" + totalAmount);
        double totalAmount_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + totalAmount), 2);
        Log.e("ROUNDINGGGGG:", "" + totalAmount_double);
        //totalAmount=totalAmount_double.floatValue();
        totalAmount = (float) totalAmount_double;
        if (totalAmount == 0.0 || totalAmount == 0) {
            totalAmount = mainTotal;
        }
        Log.e("totalAmount:totalAmount:", "" + totalAmount);
        String HSTT = "0", GSTT = "0", QSTT = "0", PSTT = "0";
        if (etHST.getText().length() == 0) {
            HSTT = "0";
        } else {
            HSTT = etHST.getText().toString();
        }
        if (etGST.getText().length() == 0) {
            GSTT = "0";
        } else {
            GSTT = etGST.getText().toString();
        }
        if (etQST.getText().length() == 0) {
            QSTT = "0";
        } else {
            QSTT = etQST.getText().toString();
        }
        if (etPST.getText().length() == 0) {
            PSTT = "0";
        } else {
            PSTT = etPST.getText().toString();
        }
        if (etHSTPercentage.getText().length() != 0) {
            HSTT = "" + mainTotal / 100 * Float.parseFloat(etHSTPercentage.getText().toString());
            double etHSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(HSTT), 2);
            HSTT = "" + etHSTPercentage_double;
            Log.e("etHSTPercentage:", "" + HSTT);

        }
        if (etGSTPercentage.getText().length() != 0) {
            Log.e("mainTotal:", "" + mainTotal);
            GSTT = "" + mainTotal / 100 * Float.parseFloat(etGSTPercentage.getText().toString());
            Log.e("GSTT before:", "" + GSTT);
            double etGSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(GSTT), 2);
            GSTT = "" + etGSTPercentage_double;
            Log.e("GSTT:", "" + GSTT);
        }
        if (etQSTPercentage.getText().length() != 0) {
            QSTT = "" + mainTotal / 100 * Float.parseFloat(etQSTPercentage.getText().toString());

            double etQSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(QSTT), 2);
            QSTT = "" + etQSTPercentage_double;
            Log.e("QSTT:", "" + QSTT);

        }
        if (etPSTPercentage.getText().length() != 0) {
            PSTT = "" + mainTotal / 100 * Float.parseFloat(etPSTPercentage.getText().toString());
            double etPSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(PSTT), 2);
            PSTT = "" + etPSTPercentage_double;
            Log.e("PSTT:", "" + PSTT);
        }
        Log.e("Round:", "" + getRoundSimple(Float.parseFloat(HSTT) + Float.parseFloat(GSTT) +
                Float.parseFloat(QSTT) + Float.parseFloat(PSTT) + totalAmount));

        return getRoundSimple(Float.parseFloat(HSTT) + Float.parseFloat(GSTT) +
                Float.parseFloat(QSTT) + Float.parseFloat(PSTT) + totalAmount);

    }


    private String getAmountResultFirstTime() {
      /*  Log.e("total-------Amount:", "" + totalAmount);
        double totalAmount_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + totalAmount), 2);
        totalAmount = (float) totalAmount_double;
        String HSTT = "0", GSTT = "0", QSTT = "0", PSTT = "0";
        if (etHST.getText().length() == 0) {
            HSTT = "0";
        } else {
            HSTT = etHST.getText().toString();
            // Log.e("HSTT:", "" + HSTT);
        }
        if (etGST.getText().length() == 0) {
            GSTT = "0";
        } else {
            GSTT = etGST.getText().toString();
            //Log.e("GSTT:", "" + GSTT);
        }
        if (etQST.getText().length() == 0) {
            QSTT = "0";
        } else {
            QSTT = etQST.getText().toString();
            // Log.e("QSTT:", "" + QSTT);
        }
        if (etPST.getText().length() == 0) {
            PSTT = "0";
        } else {
            PSTT = etPST.getText().toString();
            // Log.e("PSTT:", "" + PSTT);
        }
        if (etHSTPercentage.getText().length() != 0) {
            HSTT = "" + totalAmount / 100 * Float.parseFloat(etHSTPercentage.getText().toString());
            double etHSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(HSTT), 2);
            HSTT = "" + etHSTPercentage_double;
        }
        if (etGSTPercentage.getText().length() != 0) {
            Log.e("totalAmount:", "" + totalAmount);
            GSTT = "" + totalAmount / 100 * Float.parseFloat(etGSTPercentage.getText().toString());
            Log.e("GSTT before:", "" + GSTT);
            double etGSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(GSTT), 2);
            GSTT = "" + etGSTPercentage_double;
        }
        if (etQSTPercentage.getText().length() != 0) {
            QSTT = "" + totalAmount / 100 * Float.parseFloat(etQSTPercentage.getText().toString());
            double etQSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(QSTT), 2);
            QSTT = "" + etQSTPercentage_double;
        }
        if (etPSTPercentage.getText().length() != 0) {
            PSTT = "" + totalAmount / 100 * Float.parseFloat(etPSTPercentage.getText().toString());
            double etPSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(PSTT), 2);
            PSTT = "" + etPSTPercentage_double;
        }
        Log.e("PSTT:", "" + Float.parseFloat(PSTT));
        Log.e("totalAmount:", "" + totalAmount);
        Log.e("HSTT:", "" + Float.parseFloat(HSTT));
        Log.e("QSTT:", "" + Float.parseFloat(QSTT));
        Log.e("GSTT:", "" + Float.parseFloat(GSTT));
        Log.e("dis_Amount:", "" + dis_Amount);
        return getRoundSimple(Float.parseFloat(HSTT) + Float.parseFloat(GSTT) +
                Float.parseFloat(QSTT) + Float.parseFloat(PSTT) + totalAmount - dis_Amount);*/

        dis_Amount = Float.parseFloat(calculate_Tax_Percentage());

        float mainTotal = Float.parseFloat(calculationTotalRateAndQuantity());
        Log.e("totalAmount:", "" + totalAmount);
        double totalAmount_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble("" + totalAmount), 2);
        Log.e("ROUNDINGGGGG:", "" + totalAmount_double);

        //totalAmount=totalAmount_double.floatValue();
        totalAmount = (float) totalAmount_double;
        if (totalAmount == 0.0 || totalAmount == 0) {
            totalAmount = mainTotal;
            dis_Amount = totalAmount - dis_Amount;
        }
        Log.e("totalAmount:totalAmount:", "" + totalAmount);
        Log.e("dis_Amount::", "" + dis_Amount);
        String HSTT = "0", GSTT = "0", QSTT = "0", PSTT = "0";
        if (etHST.getText().length() == 0) {
            HSTT = "0";
        } else {
            HSTT = etHST.getText().toString();
        }
        if (etGST.getText().length() == 0) {
            GSTT = "0";
        } else {
            GSTT = etGST.getText().toString();
        }
        if (etQST.getText().length() == 0) {
            QSTT = "0";
        } else {
            QSTT = etQST.getText().toString();
        }
        if (etPST.getText().length() == 0) {
            PSTT = "0";
        } else {
            PSTT = etPST.getText().toString();
        }
        if (etHSTPercentage.getText().length() != 0) {
            HSTT = "" + mainTotal / 100 * Float.parseFloat(etHSTPercentage.getText().toString());
            double etHSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(HSTT), 2);
            HSTT = "" + etHSTPercentage_double;
            Log.e("etHSTPercentage:", "" + HSTT);

        }
        if (etGSTPercentage.getText().length() != 0) {
            Log.e("mainTotal:", "" + mainTotal);
            GSTT = "" + mainTotal / 100 * Float.parseFloat(etGSTPercentage.getText().toString());
            Log.e("GSTT before:", "" + GSTT);
            double etGSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(GSTT), 2);
            GSTT = "" + etGSTPercentage_double;
            Log.e("GSTT:", "" + GSTT);
        }
        if (etQSTPercentage.getText().length() != 0) {
            QSTT = "" + mainTotal / 100 * Float.parseFloat(etQSTPercentage.getText().toString());

            double etQSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(QSTT), 2);
            QSTT = "" + etQSTPercentage_double;
            Log.e("QSTT:", "" + QSTT);

        }
        if (etPSTPercentage.getText().length() != 0) {
            PSTT = "" + mainTotal / 100 * Float.parseFloat(etPSTPercentage.getText().toString());
            double etPSTPercentage_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(PSTT), 2);
            PSTT = "" + etPSTPercentage_double;
            Log.e("PSTT:", "" + PSTT);
        }
        Log.e("Round:", "" + getRoundSimple(Float.parseFloat(HSTT) + Float.parseFloat(GSTT) +
                Float.parseFloat(QSTT) + Float.parseFloat(PSTT) + totalAmount - dis_Amount));

        return getRoundSimple(Float.parseFloat(HSTT) + Float.parseFloat(GSTT) +
                Float.parseFloat(QSTT) + Float.parseFloat(PSTT) + totalAmount - dis_Amount);

    }

    private void removeListnerWithErrorMessage(EditText etDiscountPercentage, String error, TextWatcher textWatcher) {

        etDiscountPercentage.removeTextChangedListener(textWatcher);
        etDiscountPercentage.setText("");
        etDiscountPercentage.addTextChangedListener(textWatcher);
        CommonDialog.With(ItemDetailRejected.this).Show(error);

    }

    // percentage Watcher

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_detail_activity);
        initlization();
    }


    // HST watcher

    private void initlization() {

        generateRightText1("ITEM DETAILS");
        // Buttons
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_save.setOnClickListener(this);
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            DataDetails = extras.getString("DataDetails");
            position = extras.getString("position");

            if (DataDetails != null) {
                itemModal1 = gson.fromJson(DataDetails, AddNewItem.class);
                if (itemModal1.getServiceTypeId() != null) {
                    servicetypeValue = itemModal1.getServiceTypeId();
                    Log.e("servicetypeValue:", "" + servicetypeValue);
                }
            }

        }

        // EditText
        etItemNumber = (EditText) findViewById(R.id.etItemNumber);
        etItemNumber.setEnabled(false);
        etDescription = (EditText) findViewById(R.id.etDescription);
        etQuantityHours = (EditText) findViewById(R.id.etQuantityHours);
        etRate = (EditText) findViewById(R.id.etRate);


        etDiscountAmount = (EditText) findViewById(R.id.etDiscountAmount);
        etDiscountPercentage = (EditText) findViewById(R.id.etDiscountPercentage);

        etTax = (EditText) findViewById(R.id.etTax);
        etTaxPercentage = (EditText) findViewById(R.id.etTaxPercentage);


        etHST = (EditText) findViewById(R.id.etHST);
        etGST = (EditText) findViewById(R.id.etGST);
        etQST = (EditText) findViewById(R.id.etQST);
        etPST = (EditText) findViewById(R.id.etPST);

        etHSTPercentage = (EditText) findViewById(R.id.etHSTPercentage);
        etGSTPercentage = (EditText) findViewById(R.id.etGSTPercentage);
        etQSTPercentage = (EditText) findViewById(R.id.etQSTPercentage);
        etPSTPercentage = (EditText) findViewById(R.id.etPSTPercentage);


        tvSubTotal = (TextView) findViewById(R.id.tvSubTotal);


        // Spinner
        spinnerProductTyoe = (Spinner) findViewById(R.id.spinnerProductTyoe);
        tvServiceProductType = (TextView) findViewById(R.id.tvServiceProductType);
        tvServiceProductType.setOnClickListener(this);
        tvAmountRate = (TextView) findViewById(R.id.tvAmountRate);

        llTaxTypeLayout = (LinearLayout) findViewById(R.id.llTaxTypeLayout);

        // Relative Layout
        taxLayout = (RelativeLayout) findViewById(R.id.taxLayout);
        rlSpinnerLayout = (RelativeLayout) findViewById(R.id.rlSpinnerLayout);
        if (getInvoiceOnly()) {
            rlSpinnerLayout.setVisibility(View.GONE);
            setdData(itemModal1);

            etDiscountPercentage.addTextChangedListener(new DecimalFilter(etDiscountPercentage, activity));
            etDiscountAmount.addTextChangedListener(new DecimalFilter(etDiscountAmount, activity));
            etHST.addTextChangedListener(new DecimalFilter(etHST, activity));
            etGST.addTextChangedListener(new DecimalFilter(etGST, activity));
            etQST.addTextChangedListener(new DecimalFilter(etQST, activity));
            etPST.addTextChangedListener(new DecimalFilter(etPST, activity));
            etHSTPercentage.addTextChangedListener(new DecimalFilter(etHSTPercentage, activity));
            etGSTPercentage.addTextChangedListener(new DecimalFilter(etGSTPercentage, activity));
            etQSTPercentage.addTextChangedListener(new DecimalFilter(etQSTPercentage, activity));
            etPSTPercentage.addTextChangedListener(new DecimalFilter(etPSTPercentage, activity));
            etTax.addTextChangedListener(new DecimalFilter(etTax, activity));
            etTaxPercentage.addTextChangedListener(new DecimalFilter(etTaxPercentage, activity));


            etDiscountPercentage.addTextChangedListener(watcherTaxPercentage);
            etDiscountAmount.addTextChangedListener(watcherTaxAmount);
            etHST.addTextChangedListener(HSTWatcher);
            etGST.addTextChangedListener(GSTWatcher);
            etQST.addTextChangedListener(QSTWatcher);
            etPST.addTextChangedListener(PSTWatcher);
            etHSTPercentage.addTextChangedListener(HSTPercentageWatcher);
            etGSTPercentage.addTextChangedListener(GSTPercentageWatcher);
            etQSTPercentage.addTextChangedListener(QSTPercentageWatcher);
            etPSTPercentage.addTextChangedListener(PSTPercentageWatcher);
            etTax.addTextChangedListener(TAXWatcher);
            etTaxPercentage.addTextChangedListener(TAXPercentageWatcher);

        } else {
            checkRevenue();
            rlSpinnerLayout.setVisibility(View.VISIBLE);
        }

        if (CommonUtility.IsFinance.equals("true")) {
            taxLayout.setVisibility(View.GONE);
            llTaxTypeLayout.setVisibility(View.VISIBLE);
        } else {
            taxLayout.setVisibility(View.VISIBLE);
            llTaxTypeLayout.setVisibility(View.GONE);
        }

        settingFocus();
        // TextView


        etQuantityHours.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float rate = 0;
                float hours = 0;
                if (etQuantityHours.getText().toString().length() != 0) {
                    hours = Float.parseFloat(etQuantityHours.getText().toString());
                    if (etRate.getText().toString().length() != 0) {
                        rate = Float.parseFloat(etRate.getText().toString());
                        float total = hours * rate;
                        tvAmountRate.setText(getRoundSimple(total));
                        totalAmount = total;
                        tvSubTotal.setText("" + total);
                        if (etDiscountAmount.getText().length() != 0) {
                            tvSubTotal.setText("" + (/*getRoundSimple*/(Float.parseFloat(tvAmountRate.getText().toString())
                                    -
                                    Float.parseFloat(etDiscountAmount.getText().toString()))));
                        } else {
                            tvSubTotal.setText("" + getRoundSimple(total));
                        }


                    }

                    if (etDiscountPercentage.getText().length() != 0) {
                        float percentageValue1 = (Float.parseFloat(tvAmountRate.getText().toString()) / 100) * Float.parseFloat(etDiscountPercentage.getText().toString());
                        totalAmount = Float.parseFloat(tvAmountRate.getText().toString()) - percentageValue1;
                        tvSubTotal.setText("" + totalAmount);
                    }
                    if (etTaxPercentage.getText().length() != 0) {
                        float percentageValue1 = (Float.parseFloat(tvSubTotal.getText().toString()) / 100) * Float.parseFloat(etTaxPercentage.getText().toString());
                        totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + percentageValue1;
                        tvSubTotal.setText("" + totalAmount);
                    }

                    if (etTax.getText().toString().length() != 0) {
                        tvSubTotal.setText("" + getRoundSimple(Float.parseFloat(tvSubTotal.getText().toString()) + Float.parseFloat(etTax.getText().toString())));
                    }


                } else {
                    tvAmountRate.setText("");
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //etQuantityHours.addTextChangedListener(new DecimalFilterRateOnly(etQuantityHours, activity));
        // etRate.addTextChangedListener(new DecimalFilterRateOnly(etRate, activity));
        etRate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                float hours = 0;
                float rate = 0;
                if (etRate.getText().toString().length() != 0) {
                    rate = Float.parseFloat(etRate.getText().toString());
                    if (etQuantityHours.getText().toString().length() != 0) {
                        hours = Float.parseFloat(etQuantityHours.getText().toString());
                        float total = hours * rate;
                        tvAmountRate.setText(getRoundSimple(total));
                        totalAmount = total;
                        tvSubTotal.setText("" + total);
                        if (etDiscountAmount.getText().length() != 0) {
                            totalAmount = Float.parseFloat(tvAmountRate.getText().toString()) - Float.parseFloat(etDiscountAmount.getText().toString());
                            tvSubTotal.setText("" + getAmountResult());
                            tvSubTotal.setText("" + getAmountResult());
                        } else {
                            tvSubTotal.setText(getRoundSimple(total));
                        }

                        if (etDiscountPercentage.getText().length() != 0) {
                            float percentageValue1 = (Float.parseFloat(tvAmountRate.getText().toString())) / 100 * Float.parseFloat(etDiscountPercentage.getText().toString());
                            totalAmount = Float.parseFloat(tvAmountRate.getText().toString()) - percentageValue1;
                            tvSubTotal.setText("" + totalAmount);
                        }
                        if (etTaxPercentage.getText().length() != 0) {
                            float percentageValue1 = (Float.parseFloat(tvSubTotal.getText().toString()) / 100) * Float.parseFloat(etTaxPercentage.getText().toString());
                            totalAmount = Float.parseFloat(tvSubTotal.getText().toString()) + percentageValue1;
                            tvSubTotal.setText("" + totalAmount);
                        }


                        if (etTax.getText().toString().length() != 0) {
                            String total_sub = tvSubTotal.getText().toString().replace(",", "");
                            String tax = etTax.getText().toString().replace(",", "");
                            tvSubTotal.setText("" + getRound(Float.parseFloat(total_sub) + Float.parseFloat(tax)));
                        }


                    }

                } else {
                    tvAmountRate.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        etRate.setFilters(new InputFilter[]{new InputFilterMinMax("1", "6")});
        etQuantityHours.setFilters(new InputFilter[]{new InputFilterMinMax("1", "6")});

        spinnerProductTyoe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 && !isProductSelected) {
                    tvServiceProductType.setTextColor(Color.parseColor("#A1A1A1"));

                } else {
                    tvServiceProductType.setTextColor(Color.BLACK);
                    mProductType = revenueList.get(position).getClassificationType();
                    tvServiceProductType.setText(mProductType);
                    itemPosition = position;
                    if (revenueList.get(position).getId() != null)
                        mRevenueID = "" + revenueList.get(position).getId();
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        etRate.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        etQuantityHours.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        totalAmount = 0;
        if(etQuantityHours.getText().length()!=0){
            tvSubTotal.setText("" + getAmountResultFirstTime());
        }


    }

    private void settingFocus() {

        ((EditText) findViewById(R.id.etRate)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etRate.getText().toString().length() != 0) {
//                        double rate = Double.parseDouble(etRate.getText().toString());
                        float rate = Float.parseFloat(etRate.getText().toString());
                        etRate.setText(getRoundSimple(rate));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });
        ((EditText) findViewById(R.id.etDiscountAmount)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etDiscountAmount.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etDiscountAmount.getText().toString());
                        etDiscountAmount.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });
        ((EditText) findViewById(R.id.etDiscountPercentage)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etDiscountPercentage.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etDiscountPercentage.getText().toString());
                        etDiscountPercentage.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

        // Tax Discount
        ((EditText) findViewById(R.id.etTax)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etTax.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etTax.getText().toString());
                        etTax.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

        // Tax Percentage
        ((EditText) findViewById(R.id.etTaxPercentage)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etTaxPercentage.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etTaxPercentage.getText().toString());
                        etTaxPercentage.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

        // HST

        ((EditText) findViewById(R.id.etHST)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etHST.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etHST.getText().toString());
                        etHST.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });
        // GST
        ((EditText) findViewById(R.id.etGST)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etGST.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etGST.getText().toString());
                        etGST.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

        //
        ((EditText) findViewById(R.id.etQST)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etQST.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etQST.getText().toString());
                        etQST.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

        ((EditText) findViewById(R.id.etPST)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etPST.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etPST.getText().toString());
                        etPST.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });


        ((EditText) findViewById(R.id.etPSTPercentage)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etPSTPercentage.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etPSTPercentage.getText().toString());
                        etPSTPercentage.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

        ((EditText) findViewById(R.id.etQSTPercentage)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etQSTPercentage.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etQSTPercentage.getText().toString());
                        etQSTPercentage.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });

        ((EditText) findViewById(R.id.etGSTPercentage)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etGSTPercentage.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etGSTPercentage.getText().toString());
                        etGSTPercentage.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });
        ((EditText) findViewById(R.id.etHSTPercentage)).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (etHSTPercentage.getText().toString().length() != 0) {
                        float amt = Float.parseFloat(etHSTPercentage.getText().toString());
                        etHSTPercentage.setText(getRoundSimple(amt));
                    }
                } else {
                    Log.e("have foucs now  ", "==========");
                }
            }
        });
    }


    private void setdData(AddNewItem itemModal1) {
        etItemNumber.setText(itemModal1.getItem());

        if (itemModal1.getServiceType() != null) {
            mRevenueID = itemModal1.getServiceTypeId().toString();
            etDescription.setText(itemModal1.getDescription());
            etQuantityHours.setText(itemModal1.getQuantity().toString());
            etRate.setText(itemModal1.getRate().toString());

            if (itemModal1.getAmount() != null) {
                dAmount = Double.parseDouble(itemModal1.getAmount().toString());
                tvAmountRate.setText("" + dAmount);
                if (tvAmountRate.getText().equals("0.0")) {
                    float hours = 0;
                    float rate = 0;
                    if (etRate.getText().toString().length() != 0) {
                        rate = Float.parseFloat(etRate.getText().toString());
                        if (etQuantityHours.getText().toString().length() != 0) {
                            hours = Float.parseFloat(etQuantityHours.getText().toString());
                            float total = hours * rate;
                            tvAmountRate.setText(getRoundSimple(total));
                        }
                    }


                }
            }

            tvServiceProductType.setTextColor(Color.BLACK);
            tvServiceProductType.setText(itemModal1.getServiceType());
            mRevenueID = "" + itemModal1.getServiceTypeId();
            isProductSelected = true;

            if (itemModal1.getDiscount() != null) {
                if (!itemModal1.getDiscount().equals("")) {
                    if (checkTaxType(itemModal1.getDiscount().toString())) {
                        fDiscount = getActualValue(itemModal1.getDiscount().toString());
                        etDiscountAmount.setText(getRoundSimple(fDiscount));
                        dis_Amount = Float.parseFloat(getRoundSimple(fDiscount));
                    } else {
                        fpDiscount = getActualValue(itemModal1.getDiscount().toString());
                        etDiscountPercentage.setText(getRoundSimple(fpDiscount));
                    }
                }
            }

            if (itemModal1.getTax() != null) {
                if (!itemModal1.getTax().equals("")) {
                    if (checkTaxType(itemModal1.getTax().toString())) {
                        fTax = getActualValue(itemModal1.getTax().toString());
                        etTax.setText(getRoundSimple(fTax));
                    } else {
                        Log.e("STRING:", "" + itemModal1.getTax().toString());
                        if (itemModal1.getTax() != null) {
                            if (itemModal1.getTax().toString().equals("P_")) {

                            } else {
                                fpTax = getActualValue(itemModal1.getTax().toString());
                                etTaxPercentage.setText(getRoundSimple(fpTax));
                            }

                        }

                    }
                }
            }

            if (itemModal1.getGSTTax() != null) {
                if (!itemModal1.getGSTTax().equals("")) {
                    if (checkTaxType(itemModal1.getGSTTax().toString())) {
                        fGST = getActualValue(itemModal1.getGSTTax().toString());
                        etGST.setText(getRoundSimple(fGST));
                    } else {
                        fpGST = getActualValue(itemModal1.getGSTTax().toString());
                        etGSTPercentage.setText(getRoundSimple(fpGST));
                    }
                }
            }

            if (itemModal1.getQSTTax() != null) {
                if (!itemModal1.getQSTTax().equals("")) {
                    if (checkTaxType(itemModal1.getQSTTax().toString())) {
                        fQST = getActualValue(itemModal1.getQSTTax().toString());
                        etQST.setText(getRoundSimple(fQST));
                    } else {
                        fpQST = getActualValue(itemModal1.getQSTTax().toString());
                        etQSTPercentage.setText(getRoundSimple(fpQST));
                    }
                }
            }

            if (itemModal1.getHSTTax() != null) {
                if (!itemModal1.getHSTTax().equals("")) {
                    if (checkTaxType(itemModal1.getHSTTax().toString())) {
                        fHST = getActualValue(itemModal1.getHSTTax().toString());
                        etHST.setText(getRoundSimple(fHST));
                    } else {
                        fpHST = getActualValue(itemModal1.getHSTTax().toString());
                        etHSTPercentage.setText(getRoundSimple(fpHST));
                    }
                }
            }

            if (itemModal1.getPSTTax() != null) {
                if (!itemModal1.getPSTTax().equals("")) {
                    if (checkTaxType(itemModal1.getPSTTax().toString())) {
                        fPST = getActualValue(itemModal1.getPSTTax().toString());
                        etPST.setText(getRoundSimple(fPST));

                    } else {
                        fpPST = getActualValue(itemModal1.getPSTTax().toString());
                        etPSTPercentage.setText(getRoundSimple(fpPST));
                    }
                }
            }


            // if(getActualData(itemModal1.getGSTTax().toString()))
//        etGST.setText(itemModal1.getGSTTax().toString());
//        etQST.setText(itemModal1.getQSTTax().toString());
//        etHST.setText(itemModal1.getHSTTax().toString());
//        etPST.setText(itemModal1.getPSTTax().toString());
//        etGSTPercentage.setText(itemModal1.getGSTTax);
//        etQSTPercentage.setText(itemModal1.getQSTPercentage());
//        etHSTPercentage.setText(itemModal1.getHSTPercentage());
//        etPSTPercentage.setText(itemModal1.getPSTPercentage());
            tvSubTotal.setText(itemModal1.getSubTotal().toString());
            totalAmount = 0;
            if(etQuantityHours.getText().length()!=0){
                tvSubTotal.setText("" + getAmountResultFirstTime());
            }
           // tvSubTotal.setText("" + getAmountResultFirstTime());
            //  etTax.setText(itemModal1.getTax().toString());
            //  etTaxPercentage.setText(itemModal1.getTax());
        }
    }

    private Float getActualValue(String strTaxValue) {

        return Float.parseFloat(strTaxValue.substring(2));
    }

    /**
     * Return TRUE if TAX start with A_
     * otherwise false.
     */
    private boolean checkTaxType(String strTaxItem) {

        if (strTaxItem.length() < 0)
            return false;

        if (strTaxItem.startsWith("A_") || strTaxItem.startsWith("a_")) {
            return true;
        }
        return false;
    }

    /*private String getActualData(String strTaxData) {
        if(str)
    }*/

    // GST watcher

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                if (!getInvoiceOnly()) {
                    if (etItemNumber.getText().toString().length() != 0 && etDescription.getText().toString().length() != 0
                            && etQuantityHours.getText().toString().length() != 0
                            && etRate.getText().toString().length() != 0
                            && mRevenueID != null) {
                        itemModal = new AddNewItem();
                        itemModal.setItem(etItemNumber.getText().toString());
                        itemModal.setId(itemModal1.getId());
                        itemModal.setServiceType(mProductType);
                        try {
                            itemModal.setServiceTypeId(Integer.parseInt(mRevenueID));
                        } catch (Exception exp) {
                            itemModal.setServiceTypeId(0);
                        }
                        itemModal.setDescription(etDescription.getText().toString());
                        double etQuantityHours_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(etQuantityHours.getText().toString()), 2);
                        double etRate_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(etRate.getText().toString()), 2);
                        double tvAmountRate_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(tvAmountRate.getText().toString()), 2);
                        itemModal.setQuantity(etQuantityHours_double);
                        Log.e("etQuantityHours:", "" + etQuantityHours_double);
                        Log.e("etRate:", "" + etRate_double);
                        Log.e("tvAmountRate:", "" + tvAmountRate_double);
                        itemModal.setRate(etRate_double);
                        itemModal.setSubTotal(tvAmountRate_double);
                        itemModal.setAmount(tvAmountRate_double);
                        //itemModal.setDiscount(etDiscountAmount.getText().toString());
                        //itemModal.setDiscount(""+(float)fDiscount/*etDiscountPercentage.getText().toString()*/);
                        // itemModal.setSpinnerItemPosition(itemPosition);


                        if (etDiscountAmount.getText().toString().length() > 0) {
                            itemModal.setDiscount("A_" + "" + etDiscountAmount.getText().toString());
                        } else if (etDiscountPercentage.getText().toString().length() > 0) {
                            itemModal.setDiscount("P_" + "" + etDiscountPercentage.getText().toString());
                        } else {
                            itemModal.setDiscount("");
                        }

                        if (etHST.getText().toString().length() > 0) {
                            itemModal.setHSTTax("A_" + etHST.getText().toString());
                        } else if (etHSTPercentage.getText().toString().length() > 0) {
                            itemModal.setHSTTax("P_" + etHSTPercentage.getText().toString());
                        } else {
                            itemModal.setHSTTax("");
                        }

                        if (etGST.getText().toString().length() > 0) {
                            itemModal.setGSTTax("A_" + etGST.getText().toString());
                        } else if (etGSTPercentage.getText().toString().length() > 0) {
                            itemModal.setGSTTax("P_" + etGSTPercentage.getText().toString());
                        } else {
                            itemModal.setGSTTax("");
                        }

                        if (etQST.getText().toString().length() > 0) {
                            itemModal.setQSTTax("A_" + etQST.getText().toString());
                        } else if (etQSTPercentage.getText().toString().length() > 0) {
                            itemModal.setQSTTax("P_" + etQSTPercentage.getText().toString());
                        } else {
                            itemModal.setQSTTax("");
                        }

                        if (etPST.getText().toString().length() > 0) {
                            itemModal.setPSTTax("A_" + etPST.getText().toString());
                        } else if (etPSTPercentage.getText().toString().length() > 0) {
                            itemModal.setPSTTax("P_" + etPSTPercentage.getText().toString());
                        } else {
                            itemModal.setPSTTax("");
                        }

                        if (etTax.getText().toString().length() > 0) {

                            itemModal.setTax("A_" + etTax.getText().toString());
                        } else {

                            itemModal.setTax("P_" + etTaxPercentage.getText().toString());
                        }
                        String replace = tvSubTotal.getText().toString().replace(",", "");
                        itemModal.setSubTotal(Double.parseDouble(replace));
                        itemModal.setAmount(tvAmountRate_double);
                        Intent intent = new Intent();
                        intent.putExtra("data", itemModal);
                        intent.putExtra("position", position);
                        setResult(011, intent);
                        finish();
                        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

                    } else {
                        CommonDialog.With(ItemDetailRejected.this).Show(getResources().getString(R.string.empty_fields));
                    }
                } else {

                    if (etItemNumber.getText().toString().length() != 0 && etDescription.getText().toString().length() != 0
                            && etQuantityHours.getText().toString().length() != 0
                            /*&& etDiscountPercentage.getText().toString().length() != 0
                            || etDiscountAmount.getText().toString().length() != 0*/
                            && etRate.getText().toString().length() != 0) {
                        /*itemModal = new ItemModal();
                        itemModal.setItem(etItemNumber.getText().toString());
                        itemModal.setId(itemModal1.getId().toString());
                        itemModal.setServiceProductType(mProductType);
                        itemModal.setServiceProductId(mRevenueID);
                        itemModal.setDescription(etDescription.getText().toString());
                        itemModal.setQuantityHours(etQuantityHours.getText().toString());
                        itemModal.setRate(etRate.getText().toString());
                        itemModal.setTotalAmount(tvAmountRate.getText().toString());
                        itemModal.setDiscount(etDiscountAmount.getText().toString());
                        itemModal.setDiscountPercentage(etDiscountPercentage.getText().toString());
                        itemModal.setSpinnerItemPosition(itemPosition);

                        itemModal.setHST(etHST.getText().toString());
                        itemModal.setGST(etGST.getText().toString());
                        itemModal.setQST(etQST.getText().toString());
                        itemModal.setPST(etPST.getText().toString());

                        itemModal.setHSTPercentage(etHSTPercentage.getText().toString());
                        itemModal.setGSTPercentage(etGSTPercentage.getText().toString());
                        itemModal.setQSTPercentage(etQSTPercentage.getText().toString());
                        itemModal.setPSTPercentage(etPSTPercentage.getText().toString());

                        itemModal.setTax(etTax.getText().toString());
                        itemModal.setTaxPercentage(etTaxPercentage.getText().toString());
                        itemModal.setSubTotal(tvSubTotal.getText().toString());*/

                        itemModal = new AddNewItem();
                        itemModal.setItem(etItemNumber.getText().toString());
                        itemModal.setId(itemModal1.getId());
                        itemModal.setServiceType(mProductType);
                        try {
                            itemModal.setServiceTypeId(Integer.parseInt(mRevenueID));
                        } catch (Exception exp) {
                            itemModal.setServiceTypeId(0);
                        }
                        itemModal.setDescription(etDescription.getText().toString());
                        itemModal.setQuantity(Double.parseDouble(etQuantityHours.getText().toString()));
                        itemModal.setRate(Double.parseDouble(etRate.getText().toString()));
                        if (tvAmountRate.getText() != null) {
                            if (tvAmountRate.getText().equals("")) {
                                // itemModal.setSubTotal(Double.parseDouble(tvAmountRate.getText().toString()));
                            } else {
                                itemModal.setSubTotal(Double.parseDouble(tvAmountRate.getText().toString()));
                            }

                        }

                        //itemModal.setDiscount(etDiscountAmount.getText().toString());
                        // itemModal.setDiscount(etDiscountPercentage.getText().toString());
                        // itemModal.setSpinnerItemPosition(itemPosition);
                        // itemModal.setSpinnerItemPosition(itemPosition);

                        if (etDiscountAmount.getText().toString().length() > 0) {
                            itemModal.setDiscount("A_" + "" + etDiscountAmount.getText().toString());
                        } else if (etDiscountPercentage.getText().toString().length() > 0) {
                            itemModal.setDiscount("P_" + "" + etDiscountPercentage.getText().toString());
                        } else {
                            itemModal.setDiscount("");
                        }

                        if (etHST.getText().toString().length() > 0) {
                            itemModal.setHSTTax("A_" + etHST.getText().toString());
                        } else if (etHSTPercentage.getText().toString().length() > 0) {
                            itemModal.setHSTTax("P_" + etHSTPercentage.getText().toString());
                        } else {
                            itemModal.setHSTTax("");
                        }

                        if (etGST.getText().toString().length() > 0) {
                            itemModal.setGSTTax("A_" + etGST.getText().toString());
                        } else if (etGSTPercentage.getText().toString().length() > 0) {
                            itemModal.setGSTTax("P_" + etGSTPercentage.getText().toString());
                        } else {
                            itemModal.setGSTTax("");
                        }

                        if (etQST.getText().toString().length() > 0) {
                            itemModal.setQSTTax("A_" + etQST.getText().toString());
                        } else if (etQSTPercentage.getText().toString().length() > 0) {
                            itemModal.setQSTTax("P_" + etQSTPercentage.getText().toString());
                        } else {
                            itemModal.setQSTTax("");
                        }

                        if (etPST.getText().toString().length() > 0) {
                            itemModal.setPSTTax("A_" + etPST.getText().toString());
                        } else if (etPSTPercentage.getText().toString().length() > 0) {
                            itemModal.setPSTTax("P_" + etPSTPercentage.getText().toString());
                        } else {
                            itemModal.setPSTTax("");
                        }

                        if (etTax.getText().toString().length() > 0) {

                            itemModal.setTax("A_" + etTax.getText().toString());
                        } else if (etTaxPercentage.getText().toString().length() > 0) {

                            itemModal.setTax("P_" + etTaxPercentage.getText().toString());
                        } else {
                            itemModal.setTax("");
                        }

                        double tvAmountRate_double = CommonNumberFormatter.roundBigDecimal(Double.parseDouble(tvAmountRate.getText().toString()), 2);
                        itemModal.setAmount(tvAmountRate_double);

                        Intent intent = new Intent();
                        intent.putExtra("data", itemModal);
                        intent.putExtra("position", position);
                        setResult(011, intent);
                        finish();
                        overridePendingTransition(R.anim.animation_from_left, R.anim.animation_to_right);

                    } else {
                        CommonDialog.With(ItemDetailRejected.this).Show(getResources().getString(R.string.empty_fields));
                    }
                }
                break;
            case R.id.tvServiceProductType:
                spinnerProductTyoe.performClick();
                break;

        }
    }

    // QST watcher

    private void checkRevenue() {

        if (getInvoiceOnly()) {
            finance = "1";
        } else {
            finance = "2";
        }


        LoadingBox.showLoadingDialog(activity, "");
        Log.e("finance:", "" + finance);
        Log.e("getUserId():", "" + getUserId());
        RestClient.getApiFinanceServiceForPojo().getRevenueList(getUserId(), finance, new Callback<JsonElement>() {
                    @Override
                    public void success(JsonElement jsonElement, Response response) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("RestClient checkRevenue ", jsonElement.toString() + " \n " + response.getUrl());
                        revenueList = gson.fromJson(jsonElement.toString(), new TypeToken<List<Revenue>>() {
                        }.getType());


                        Revenue modalForIndutry = new Revenue();
                        modalForIndutry.setCategoryId(0);
                        modalForIndutry.setClassificationType("Service or Product type");
                        modalForIndutry.setChartAccountNumber(null);
                        revenueList.add(0, modalForIndutry);
                        Log.e("SIZE:", "" + revenueList.size());
                        goodsAdapter = new GoodsAdapter(ItemDetailRejected.this, revenueList, 1, true);
                        spinnerProductTyoe.setAdapter(goodsAdapter);
                        setdData(itemModal1);

                        etDiscountPercentage.addTextChangedListener(new DecimalFilter(etDiscountPercentage, activity));
                        etDiscountAmount.addTextChangedListener(new DecimalFilter(etDiscountAmount, activity));
                        etHST.addTextChangedListener(new DecimalFilter(etHST, activity));
                        etGST.addTextChangedListener(new DecimalFilter(etGST, activity));
                        etQST.addTextChangedListener(new DecimalFilter(etQST, activity));
                        etPST.addTextChangedListener(new DecimalFilter(etPST, activity));
                        etHSTPercentage.addTextChangedListener(new DecimalFilter(etHSTPercentage, activity));
                        etGSTPercentage.addTextChangedListener(new DecimalFilter(etGSTPercentage, activity));
                        etQSTPercentage.addTextChangedListener(new DecimalFilter(etQSTPercentage, activity));
                        etPSTPercentage.addTextChangedListener(new DecimalFilter(etPSTPercentage, activity));
                        etTax.addTextChangedListener(new DecimalFilter(etTax, activity));
                        etTaxPercentage.addTextChangedListener(new DecimalFilter(etTaxPercentage, activity));


                        etDiscountPercentage.addTextChangedListener(watcherTaxPercentage);
                        etDiscountAmount.addTextChangedListener(watcherTaxAmount);
                        etHST.addTextChangedListener(HSTWatcher);
                        etGST.addTextChangedListener(GSTWatcher);
                        etQST.addTextChangedListener(QSTWatcher);
                        etPST.addTextChangedListener(PSTWatcher);
                        etHSTPercentage.addTextChangedListener(HSTPercentageWatcher);
                        etGSTPercentage.addTextChangedListener(GSTPercentageWatcher);
                        etQSTPercentage.addTextChangedListener(QSTPercentageWatcher);
                        etPSTPercentage.addTextChangedListener(PSTPercentageWatcher);
                        etTax.addTextChangedListener(TAXWatcher);
                        etTaxPercentage.addTextChangedListener(TAXPercentageWatcher);
                       /* if (itemModal1.getServiceTypeId() != null) {

                        }*/
                        for (int i = 0; i < revenueList.size(); i++) {
                            if (revenueList.get(i).getId() != null)
                                if (revenueList.get(i).getId() == ItemDetailRejected.servicetypeValue) {
                                    spinnerProductTyoe.setSelection(i);
                                    //holder.label.setText(revenueList.get(position).getClassificationType());
                                }

                        }

                    }

                    @Override
                    public void failure(RetrofitError error) {
                        LoadingBox.dismissLoadingDialog();
                        Log.e("Failure", error.getUrl() + "");
                    }
                }

        );
    }

}
