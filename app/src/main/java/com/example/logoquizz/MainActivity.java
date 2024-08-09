package com.example.logoquizz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


import  java.util.Random;

import java.util.Collections;
class Node{
    Integer img;
    Node next;
    Node prev;
    boolean isCorrect;
    String correct_option;
    String options[];

    int selectedRb;

    Node(Integer img,String correct_option){
        this.img = img;
        this.correct_option = correct_option;
        this.next = null;
        this.prev = null;
        this.isCorrect = false;
        options = null;
        this.selectedRb = -1;
    }
}

class DoublyLinkedList{
    Node head;
    Node tail;

    Node current;

    DoublyLinkedList(){
        head = null;
        tail = null;
        current = head;
    }

    void insertAtEnd(Integer img,String correct_option, String options[]) {
        if (this.head == null) {
            Node newNode = new Node(img, correct_option);
            newNode.options = options;
            head = newNode;
            tail = newNode;
            current = head;
        } else {
            Node newNode = new Node(img, correct_option);
            newNode.options = options;
            newNode.prev = tail;
            tail.next = newNode;
            tail = newNode;
        }
    }

    Node getCurrent(){
        return  this.current;
    }

    void nextCurrent(){
        if (this.current.next != null){
            this.current = this.current.next;
        }
    }

    void prevCurrent(){
        if (this.current.prev != null){
            this.current = this.current.prev;
        }
    }

    void setSelectedRB(int radioBtn){
        this.current.selectedRb = radioBtn;
    }

    void setIsCorrect(boolean flag){
        this.current.isCorrect = flag;

    }

    int getScore(){
        Node temp = head;
        int score =0;
        while(temp!=null){
            if(temp.isCorrect){
                score++;
            };
            temp=temp.next;
        }
        return score;
    }

    public void deleteAll() {

        head = null;
        tail = null;
        current = null;
    }
}

public class MainActivity extends AppCompatActivity {

    private ImageView img;

    private RadioButton op1,op2,op3,op4;

    private RadioGroup rgb;
    private DoublyLinkedList myList;
    private Random rand;

    Button btnNext,btnPrev;
    HashMap<String,Integer> imgHashMap;

    int totalRoundImg =10;

    int totalLogoCount = 44;

    private Button selectedBtn;

    private TextView score;

    ArrayList<String> allLogoNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        start();
    }
    private void init(){

//        init vars...
        myList = new DoublyLinkedList();
        imgHashMap= new HashMap<String,Integer>();
        rand = new Random();

        createHashMap();

        img= findViewById(R.id.imgLogo);
        op1 = findViewById(R.id.op1);
        op2 = findViewById(R.id.op2);
        op3 = findViewById(R.id.op3);
        op4 = findViewById(R.id.op4);
        btnNext = findViewById(R.id.btnNext);
        btnPrev = findViewById(R.id.btnPrev);
        rgb = findViewById(R.id.rbg);
        score = findViewById(R.id.score);


        rgb.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                Log.d("ID", "onCheckedChanged: "+checkedId+" "+ rgb.getCheckedRadioButtonId());

                if (myList.current.selectedRb == checkedId){
                    return;
                }
                selectedBtn = findViewById(checkedId);
                myList.setSelectedRB(checkedId);
                if(selectedBtn != null){
                    if(myList.current.correct_option.equals(selectedBtn.getText())){
                        myList.setIsCorrect(true);
                    }
                    else{
                        myList.setIsCorrect(false);
                    }
                }
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (btnNext.getText().equals("Submit")) {
                    Intent submitAcIntent = new Intent(getApplicationContext(), submitActivity.class);
                    submitAcIntent.putExtra("Total", totalRoundImg);
                    int correct = myList.getScore();
                    submitAcIntent.putExtra("Correct",correct);
                    submitAcIntent.putExtra("Wrong",totalRoundImg-correct);
                    startActivity(submitAcIntent);

                    finish();

                } else {
                    myList.nextCurrent();
                    changeBtnState();
                    showData();
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myList.prevCurrent();
                changeBtnState();
                showData();
            }
        });
    }

    private  void start(){

        //adding all logo name to string array for random options
        addOptions();

        //adding images to linked list
       addNodesToList();

       showData();

       changeBtnState();
    }

    private String[] getOptions(String correct_ans){
        String options[] = new String[4];
        options[0] = correct_ans;
        options[1]="a";
        options[2]="b";
        options[3]="c";
        int i=1,index;
        boolean isIn;
        int j=0;
        while(i<4){
            index = rand.nextInt(totalLogoCount);
            isIn = false;
            for(j = 0;j<4;j++){
                if(allLogoNames.get(index).equals(options[j])){
                    isIn = true;
                    break;
                }
            }
            if(!isIn){
                options[i] = allLogoNames.get(index);
                i++;
            }
        }
        shuffleArray(options);
        Log.d("OPTIONS", "getOptions: "+options[0]);
        Log.d("OPTIONS", "getOptions: "+options[1]);
        Log.d("OPTIONS", "getOptions: "+options[2]);
        Log.d("OPTIONS", "getOptions: "+options[3]);
        return options;
    }

    void shuffleArray(String[] ar)
    {
        int i=0;

        for (i = ar.length - 1; i > 0; i--)
        {
            int index = rand.nextInt(i + 1);
            // Simple swap
            String a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private void addOptions(){
        allLogoNames = new ArrayList<>(imgHashMap.keySet());
        Collections.shuffle(allLogoNames);
    }

    private void addNodesToList(){

        int i=0;
        for(String key: allLogoNames){
            if (i == totalRoundImg){
                break;
            }
            myList.insertAtEnd(imgHashMap.get(key), key,getOptions(key));
            i++;
        }
    }
    private void createHashMap(){
        imgHashMap.put("Adidas",R.drawable.adidas);
        imgHashMap.put("Adobe XD",R.drawable.adobe_xd);
        imgHashMap.put("Amazon",R.drawable.amazon);
        imgHashMap.put("Angular",R.drawable.angular);
        imgHashMap.put("Audi",R.drawable.audi);
        imgHashMap.put("Bentley",R.drawable.bentley);
        imgHashMap.put("Chanel",R.drawable.chanel);
        imgHashMap.put("CSS",R.drawable.css);
        imgHashMap.put("Ethereum",R.drawable.ethereum);
        imgHashMap.put("Facebook",R.drawable.facebook);
        imgHashMap.put("Facebook Messenger",R.drawable.facebook_messanger);
        imgHashMap.put("Ferrari",R.drawable.ferrari);
        imgHashMap.put("Figma",R.drawable.figma);
        imgHashMap.put("Fiat",R.drawable.fiat);
        imgHashMap.put("Github",R.drawable.github);
        imgHashMap.put("Gmail",R.drawable.gmail);
        imgHashMap.put("Google",R.drawable.google);
        imgHashMap.put("Hello Kitty",R.drawable.hello_kitty);
        imgHashMap.put("Honda",R.drawable.honda);
        imgHashMap.put("Instagram",R.drawable.instagram);
        imgHashMap.put("Jaguar",R.drawable.jaguar);
        imgHashMap.put("Jordan",R.drawable.jordan);
        imgHashMap.put("Lacoste",R.drawable.lacoste);
        imgHashMap.put("Mazda",R.drawable.mazda);
        imgHashMap.put("McDonald",R.drawable.mcdonald);
        imgHashMap.put("Mercedes",R.drawable.mercedes);
        imgHashMap.put("Microsoft Teams",R.drawable.microsoft_teams);
        imgHashMap.put("Mitsubishi",R.drawable.mitsubishi);
        imgHashMap.put("Nike",R.drawable.nike);
        imgHashMap.put("Opel",R.drawable.opel);
        imgHashMap.put("photoshop",R.drawable.photoshop);
        imgHashMap.put("Porsche",R.drawable.porsche);
        imgHashMap.put("Rasberry Pi",R.drawable.raspberry_pi);
        imgHashMap.put("Red Bull",R.drawable.redbull);
        imgHashMap.put("Renault",R.drawable.renault);
        imgHashMap.put("Shell",R.drawable.shell);
        imgHashMap.put("Skoda",R.drawable.skoda);
        imgHashMap.put("Snapchat",R.drawable.snapchat);
        imgHashMap.put("Telegram",R.drawable.telegram);
        imgHashMap.put("Twitter",R.drawable.twitter);
        imgHashMap.put("Volkswagen",R.drawable.volkswagen);
        imgHashMap.put("Volvo",R.drawable.volvo);
        imgHashMap.put("Whatsapp",R.drawable.whatsapp);
        imgHashMap.put("Wonder",R.drawable.wonder);
    }

    private void showData(){

        img.setImageResource(myList.current.img);
        op1.setText(myList.current.options[0]);
        op2.setText(myList.current.options[1]);
        op3.setText(myList.current.options[2]);
        op4.setText(myList.current.options[3]);

        if(myList.current.selectedRb == -1){
            rgb.clearCheck();
        }else{
            rgb.check(myList.current.selectedRb);
        }
    }

    void changeBtnState(){
        if(myList.current.next == null){
            btnNext.setText("Submit");
        }
        else{
            btnNext.setEnabled(true);
        }

        if(myList.current.prev == null){
            
            btnPrev.setEnabled(false);
        }
        else{
            btnPrev.setEnabled(true);
        }
    }

}