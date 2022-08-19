
 import { initializeApp } from "https://www.gstatic.com/firebasejs/9.9.1/firebase-app.js";
 import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.9.1/firebase-analytics.js";
 
 import { } from "https://www.gstatic.com/firebasejs/9.9.1/firebase-app.js";
 import { getFirestore, doc, getDoc, getDocs, setDoc, collection, addDoc, updateDoc, deleteDoc, deleteField, query, where } from "https://www.gstatic.com/firebasejs/9.9.1/firebase-firestore.js";
 
 // TODO: Add SDKs for Firebase products that you want to use
 // https://firebase.google.com/docs/web/setup#available-libraries
 
 // Your web app's Firebase configuration
 // For Firebase JS SDK v7.20.0 and later, measurementId is optional
 const firebaseConfig = {
   apiKey: "AIzaSyDP_JD9a91uuReYAd61-oCss1hzLIQyg2s",
   authDomain: "store-ma.firebaseapp.com",
   projectId: "store-ma",
   storageBucket: "store-ma.appspot.com",
   messagingSenderId: "664769599197",
   appId: "1:664769599197:web:c9c85f15b20f50a694ed34",
   measurementId: "G-W7C0LD2T4Q"
 };
 
 // Initialize Firebase
 const app = initializeApp(firebaseConfig);
 
 const db = getFirestore();



 let phoneNumber;
 let itemId; 

 let itemName = document.getElementById("itemName");
 let itemImage = document.getElementById("itemImage");
 let mainCategory = document.getElementById("mainCategory");
 let discount = document.getElementById("category_name");
 let mainDescription = document.getElementById("mainDescription");
 let item_description = document.getElementById("item_description");
 let itemPrice = document.getElementById("itemPrice");

 let price;
 let allTextElements = document.getElementById("number_of_items");
 let decreaseButton = document.getElementById("view-button");
 let increaseButton = document.getElementById("buy-button");

 let goBackButton = document.getElementById("goBack");

 let numberOfItems = 0;

 let gst_per;

 let secondTotalOrder=[];
 let isPresentInOders = "0";
 let secondOrder = [];

//  let sessionData=[];
//  let totalOrders=[];






 phoneNumber = sessionStorage.getItem('phone');
 itemId = sessionStorage.getItem('clickedItem');
 secondTotalOrder = JSON.parse(sessionStorage.getItem('totalOrders'));



 setItemNumberFromLocalStorage();
 getItemDetails();
 allTextElements.innerText=numberOfItems+'';

 decreaseButton.addEventListener('click' , ()=>{

    if(allTextElements.innerText =="0"){

    }else{
        allTextElements.innerText = parseInt(allTextElements.innerText)-1+'';
    }

    

 });

 increaseButton.addEventListener('click' , ()=>{

    allTextElements.innerText = parseInt(allTextElements.innerText)+1+'';

 });


 


 goBackButton.onclick =  function returnWhatItemSelected(){

    sessionStorage.setItem('clickedItem', itemId);
    sessionStorage.setItem('numberWeGot', allTextElements.innerText+'');
    sessionStorage.setItem('isReturning', '1');


    if(isPresentInOders == "1"){



        for(let i = 0; i<secondTotalOrder.length; i++){

            if(secondTotalOrder[i][0] == itemId){

                

                secondTotalOrder[i][1] =allTextElements.innerText+'';
                break;

            }

        }

    }else{
        
        secondOrder[0] = itemId;
        secondOrder[1] = allTextElements.innerText+'';
        secondOrder[2] = itemPrice.innerText+'';
        secondOrder[3] = itemName.innerText+'';
        secondOrder[4] = itemImage.src + '';
        secondOrder[5] = (((parseFloat(gst_per.innerText))/100)*parseFloat(itemPrice.innerText))+'';

        secondTotalOrder.push(secondOrder);

    }

    sessionStorage.setItem('totalOrders', JSON.stringify(secondTotalOrder));
    history.back();



 }


 

//  setTimeout(function(){ 

//     ;
//     totalOrders = JSON.parse(sessionStorage.getItem('totalOrders'));
//     
//  for(let i = 0; i<sessionData.length; i++){
//      for(let y= 0; y< parseInt(sessionData[i][1]); y++){

//          getItemIdFromCards(sessionData[i][0]+"", true);

//      }
     
//  }


//   }, 1000);

 



 async function getItemDetails() {
    const ref = doc(db, "_"+phoneNumber+"_items", itemId);

    const documentSnap = await getDoc(ref);

    if(documentSnap.exists()){

        itemName.innerText = documentSnap.data().item_name;

        itemImage.src = documentSnap.data().item_image_1;

        mainCategory.innerText = "Catgory: "+documentSnap.data().item_category_text;

        if(documentSnap.data().is_discount_in_percentage == "1"){
            discount.innerText = (documentSnap.data().discount)+' % off';
            item_description.innerText ='Rs. '+(documentSnap.data().mrp);
        } else if(documentSnap.data().is_discount_in_percentage == "0"){
            if(documentSnap.data().discount=="0"){
                discount.innerText = 'Today\'s Price: ';
                discount.style.color = '#000';
                discount.style.backgroundColor = "#F2F2F7";
                discount.style.paddingLeft = 0;
                item_description.hidden = true;
            }else {
                discount.innerText = 'Flat Rs. '+(documentSnap.data().discount)+' off';
                item_description.innerText ='Rs. '+(documentSnap.data().mrp);
            }
            
        }


        
        if(documentSnap.data().is_discount_in_percentage == "1"){
            price = parseFloat(documentSnap.data().mrp) - ((parseFloat(documentSnap.data().discount)/100)*parseFloat(documentSnap.data().mrp));
            price = ""+price;
        } else {
            price = parseFloat(documentSnap.data().mrp)-parseFloat(documentSnap.data().discount);
            price = ""+price;
        }

        itemPrice.innerText = "Rs. "+price;

        mainDescription.innerHTML = documentSnap.data().item_description;

        gst_per = documentSnap.data().item_gst;



    }


 }


 function setItemNumberFromLocalStorage(){

    if(sessionStorage.getItem('totalNumber') == 'notSet'){
        numberOfItems = 0;
        isPresentInOders="0";
    }else {
        numberOfItems = parseInt(sessionStorage.getItem('totalNumber'));
        isPresentInOders = "1";
    }
    

 } 







//  function getItemIdFromCards(buttonItemId, increase) {

//     let order =[];
//     let things;
    
//     let isDone = false;
//     let allCardElements = document.getElementsByClassName("card");
//     let itemPrice = document.querySelector("h2[name='"+buttonItemId+"_price']");
//     let titleOfItem = document.querySelector("h1[name='"+buttonItemId+"_name']");
//     let imageOfItem = document.querySelector("img[name='"+buttonItemId+"_image']");
    



//     if(increase){

        
//     if(totalOrders.length !== 0){

//         for(let k = 0; k < totalOrders.length; k++){
    
//             if(totalOrders[k][0] === buttonItemId){
//                 things = parseInt(totalOrders[k][1]) + 1;
//                 totalOrders[k][1]  = things + '';
//                 allTextElements.innerText = totalOrders[k][1]+'';
                
                
//                 isDone = true;
//                 break;
//             }
//         }
//     } 
    
//         if(!isDone) {
//             
//             order[0] = buttonItemId;
//             order[1] = 1+'';
//             order[2] = itemPrice.innerText+'';
//             order[3] = titleOfItem.innerText+'';
//             order[4] = imageOfItem.src + '';
//             totalOrders.push(order);
//             allTextElements.innerText = totalOrders[totalOrders.length-1][1]+'';

            
//             changeCardBackground(buttonItemId,'1');
            
            
    
     
//     }
        
//     } else {

//         if(totalOrders.length !== 0){

//             for(let k = 0; k < totalOrders.length; k++){
        
//                 if(totalOrders[k][0] === buttonItemId){
//                     if(totalOrders[k][1]=='1'){
//                         totalOrders.remove(k);
                        
//                         allTextElements.innerText = 0+'';
                        
                        
//                     } else {
//                         things = parseInt(totalOrders[k][1]) - 1;
//                     totalOrders[k][1]  = things + '';
//                     allTextElements.innerText = totalOrders[k][1]+'';
                   

//                     }
                    
//                     isDone = true;
//                     break;
//                 }
//             }
//         }
        
//         
        
//             if(!isDone) {
//                 // 
//                 // order[0] = buttonItemId;
//                 // order[1] = 1+'';
//                 // totalOrders.push(order);
//                 // allTextElements.innerText = totalOrders[totalOrders.length-1][1]+'';
//                 // changeCardBackground(buttonItemId);
                
                
        
         
//         }

//     }



// 
// sessionStorage.setItem('totalOrders', JSON.stringify(totalOrders));

//  }

