package com.example.batterylowmessenger.viewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.batterylowmessenger.App;
import com.example.batterylowmessenger.LoadData;
import com.example.batterylowmessenger.R;
import com.example.batterylowmessenger.data.Contact;
import com.example.batterylowmessenger.repository.ContactsRepository;
import com.example.batterylowmessenger.sharedPreferenceStorage.ApplicationSharedPreference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class InfoFragmentViewModel extends ViewModel {

    @Inject
    Context context;
    @Inject
    ContactsRepository repository;


    public ObservableField<String> checkedContactString = new ObservableField<>();
    public ObservableField<String>messageText = new ObservableField<>();
    public ObservableField<String>batteryLevelText = new ObservableField<>();

    public InfoFragmentViewModel() {
        App.getAppComponent().inject(this);
    }

    public void start(){
        repository.getCheckedContactList(new LoadData.LoadContactCallback() {
            @Override
            public void onContactsLoaded(List<Contact> tasks) {
                StringBuilder builder = new StringBuilder();
                builder.append(context.getResources().getString(R.string.selected_contacts));
                builder.append("\n");
                for(Contact contact:tasks){
                    builder.append(contact.getContactName());
                    builder.append("\n");
                }
                checkedContactString.set(builder.toString());

            }

            @Override
            public void onDataNotAvailable() {
                StringBuilder builder = new StringBuilder();
                builder.append(context.getResources().getString(R.string.selected_contacts));
                builder.append("\n");
            }
        });

        if(ApplicationSharedPreference.getStoredMessage(context)!=null){
            StringBuilder builderMessage = new StringBuilder();
            builderMessage.append(context.getResources().getString(R.string.selected_message));
            builderMessage.append("\n");
            builderMessage.append(ApplicationSharedPreference.getStoredMessage(context));
            messageText.set(builderMessage.toString());
        }

        populateBatteryLevelField();
    }

    private void populateBatteryLevelField(){
        StringBuilder builderBatteryLevel = new StringBuilder();
        builderBatteryLevel.append(context.getResources().getString(R.string.selected_batterylevel));
        builderBatteryLevel.append("\n");
        if(ApplicationSharedPreference.getStoredBatteryLevel(context)!=null){
            if(ApplicationSharedPreference.getStoredBatteryLevel(context).startsWith("0")){
                builderBatteryLevel.append(ApplicationSharedPreference.getStoredBatteryLevel(context).substring(1));
            }
            else builderBatteryLevel.append(ApplicationSharedPreference.getStoredBatteryLevel(context));
        }
        else {
            builderBatteryLevel.append(context.getResources().getString(R.string.default_batterylevel));
        }
        builderBatteryLevel.append(context.getResources().getString(R.string.percent_symbol));
        batteryLevelText.set(builderBatteryLevel.toString());
    }

}