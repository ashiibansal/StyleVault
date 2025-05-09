package com.example.loginpage;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ComingSoonActivity extends AppCompatActivity {

    RecyclerView chatRecyclerView;
    EditText userInput;
    Button sendButton, backButton;
    ChatAdapter chatAdapter;
    List<Message> messageList;

    SessionsClient sessionsClient;
    SessionName session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coming_soon);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        userInput = findViewById(R.id.userInput);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setEnabled(false);

        backButton.setOnClickListener(v -> finish());

        sendButton.setOnClickListener(v -> sendUserMessage());

        userInput.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                sendUserMessage();
                return true;
            }
            return false;
        });

        initDialogflow();
    }

    private void sendUserMessage() {
        String message = userInput.getText().toString().trim();
        if (!message.isEmpty()) {
            addMessage(message, Message.TYPE_USER);
            userInput.setText("");
            sendMessage(message);
        } else {
            Toast.makeText(this, "Please type a message", Toast.LENGTH_SHORT).show();
        }
    }

    private void initDialogflow() {
        new Thread(() -> {
            try {
                InputStream stream = getAssets().open("svchatbot.json");
                GoogleCredentials credentials = GoogleCredentials.fromStream(stream);
                SessionsSettings sessionsSettings = SessionsSettings.newBuilder()
                        .setCredentialsProvider(() -> credentials)
                        .build();
                sessionsClient = SessionsClient.create(sessionsSettings);
                session = SessionName.of("aayushi-nmjp", UUID.randomUUID().toString());

                runOnUiThread(() -> {
                    sendButton.setEnabled(true);
                    Toast.makeText(this, "Chatbot ready!", Toast.LENGTH_SHORT).show();
                });
            } catch (Exception e) {
                Log.e("DialogflowInit", "Error: ", e);
                runOnUiThread(() -> Toast.makeText(this, "Dialogflow init error: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    private void sendMessage(String message) {
        new Thread(() -> {
            try {
                TextInput textInput = TextInput.newBuilder()
                        .setText(message)
                        .setLanguageCode("en-US")
                        .build();

                QueryInput queryInput = QueryInput.newBuilder()
                        .setText(textInput)
                        .build();

                DetectIntentRequest request = DetectIntentRequest.newBuilder()
                        .setSession(session.toString())
                        .setQueryInput(queryInput)
                        .build();

                DetectIntentResponse response = sessionsClient.detectIntent(request);
                String botReply = response.getQueryResult().getFulfillmentText();

                runOnUiThread(() -> addMessage(botReply, Message.TYPE_BOT));
            } catch (Exception e) {
                Log.e("DialogflowError", "Exception: ", e);
                runOnUiThread(() -> addMessage("Sorry, something went wrong.", Message.TYPE_BOT));
            }
        }).start();
    }

    private void addMessage(String text, int type) {
        messageList.add(new Message(text, type));
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        chatRecyclerView.scrollToPosition(messageList.size() - 1); // Auto scroll
    }
}
