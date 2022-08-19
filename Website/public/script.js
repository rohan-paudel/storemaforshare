




 import { initializeApp } from "https://www.gstatic.com/firebasejs/9.9.1/firebase-app.js";
 import { getAnalytics } from "https://www.gstatic.com/firebasejs/9.9.1/firebase-analytics.js";

 import { getAuth, RecaptchaVerifier, signInWithPhoneNumber } from "https://www.gstatic.com/firebasejs/9.9.1/firebase-auth.js";
 
 import {  } from "https://www.gstatic.com/firebasejs/9.9.1/firebase-app.js";
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
 const auth = getAuth();
 
 const db = getFirestore();
 
 
 //------------------Regerences ------------------------------------//
 
 let companyName = document.getElementById("main_web_page");
 let orderNow = document.getElementById("bottom_bar");
 let businessActiveInformationText = document.getElementById("business_active_information");
 let businessaddr = document.getElementById("business_address");
 let bussgstin = document.getElementById("gstin");
 let bussdes = document.getElementById("business_des");
 let bussfield1 = document.getElementById("field1");
 let bussfield2 = document.getElementById("field2");
 let bussfield3 = document.getElementById("field3");
 let phone = document.getElementById("phone");
 let orderooo = document.getElementById("orderooo");
 let showDetails = document.getElementById("showDetails");
 let detta = document.getElementById("detta");
 let form = document.getElementById('form');

 let price_details = document.getElementById('price_details');

 let all_clear = document.getElementById('all_clear');







 let phoneNumber ="";
 let deliveryCharges;

 let storeLink = "";

 //---------- items (item_id, item_name, item_price, category_text, item_description, item_image_1) ---------------//
 let item = [];
 let items = [];
 let totalOrders = [];

 let sessionData = [];


 let totalNumberOfAllItems = 0;
 let totalNumberOfSelection = 0;




 getABusinessName();


 setTimeout(function(){ 
    getDataFromLocalStorage(); 

}, 2000);


showDetails.onclick = function(){

    if(detta.hidden == true){
        detta.hidden = false;
        showDetails.innerText = 'Hide details';
    }
    else{
        detta.hidden = true;
        showDetails.innerText = 'Show details';


    }



}
    
 
    




 function businessIsNotAcceptingOrdersNow() {

    businessActiveInformationText.innerHTML= "Sorry, the business is not active currently. <br><br>You may contact to business owner for more information.";
    orderNow.hidden = true;

 }



 async function getABusinessName() {

    var getUrl = window.location.search.slice(7);


     const ref = doc(db, "all_website_data", getUrl);

    //  const ref = doc(db, "all_website_data", "orionisdigitalcorporation7764");

     const documentSnap = await getDoc(ref);
 
     if(documentSnap.exists()){

        

        phoneNumber = documentSnap.data().phone_number;
        // phoneNumber = "9864987764";

        
        
         
 
         const refBusiness = doc(db, "_"+phoneNumber+"_business", "business_details");
        
         
         const businessDocumentSnap = await getDoc(refBusiness);
 
         if(businessDocumentSnap.exists()){
             companyName.innerText = businessDocumentSnap.data().business_name;
             deliveryCharges = businessDocumentSnap.data().delivery_charges;
             storeLink = businessDocumentSnap.data().store_link;

             phone.innerHTML = '<b>Contact : </b>'+businessDocumentSnap.data().mobile;
             
            
             if( businessDocumentSnap.data().business_address ==""){
                businessaddr.hidden = true;
             } else{
                businessaddr.innerHTML = '<b>Address : </b>'+businessDocumentSnap.data().business_address;
             }


             if( businessDocumentSnap.data().business_gst ==""){
                bussgstin.hidden = true;
             } else{
                bussgstin.innerHTML = '<b>GSTIN : </b>'+ businessDocumentSnap.data().business_gst;
             }


             if( businessDocumentSnap.data().business_description ==""){
                bussdes.hidden = true;
             } else{
                bussdes.innerHTML = '<b>Description : </b>'+businessDocumentSnap.data().business_description;
             }

             if( businessDocumentSnap.data().extra_field_value_1 ==""){
                bussfield1.hidden = true;
             } else{
                bussfield1.innerHTML ='<b>'+businessDocumentSnap.data().extra_field_title_1+' : </b>'+ businessDocumentSnap.data().extra_field_value_1;
             }


             if( businessDocumentSnap.data().extra_field_value_2 ==""){
                bussfield2.hidden = true;
             } else{
                bussfield2.innerHTML ='<b>'+businessDocumentSnap.data().extra_field_title_2+' : </b>'+ businessDocumentSnap.data().extra_field_value_2;
             }


             if( businessDocumentSnap.data().extra_field_value_3 ==""){
                bussfield3.hidden = true;
             } else{
                bussfield3.innerHTML ='<b>'+businessDocumentSnap.data().extra_field_title_3+' : </b>'+ businessDocumentSnap.data().extra_field_value_3;
             }

             

             if(businessDocumentSnap.data().is_business_active == "1"){
                getAllItems();
             } else {
                businessIsNotAcceptingOrdersNow();
             }

             
         } else {
             
         }
 
     } else {

        
 
     }
 
 }





 async function getAllItems() {
    let textItemName;
    let textItemId;
    let textItemCategory;
    let textItemPrice;
    let textItemDescription;
    let textItemImage;
    let gssst;
   

    const queryAllItems = query(collection(db, "_"+phoneNumber+"_items"), where ("is_item_active", "==", "1"));
    
    const querySnapshot = await getDocs(queryAllItems);
    querySnapshot.forEach((doc) => {

        item[0] = (doc.data().item_id);
        
        item[1] = (doc.data().item_name);
        let price;
        if(doc.data().is_discount_in_percentage == "1"){
            price = parseFloat(doc.data().mrp) - ((parseFloat(doc.data().discount)/100)*parseFloat(doc.data().mrp));
            price = ""+price;
        } else {
            price = parseFloat(doc.data().mrp)-parseFloat(doc.data().discount);
            price = ""+price;
        }
  
        item[2] = (price);
        if(doc.data().is_discount_in_percentage == "1"){
            item[3] = (doc.data().discount)+' % off';
            item[4] ='Rs. '+(doc.data().mrp);
        } else if(doc.data().is_discount_in_percentage == "0"){
            if(doc.data().discount=="0"){
                item[3] = '0';
                item[4] ='0';
            }else {
                item[3] = 'Flat Rs. '+(doc.data().discount)+' off';
                item[4] ='Rs. '+(doc.data().mrp);
            }
            
        }
        

        
        item[5] = (doc.data().item_image_1);
        item[6] = doc.data().item_gst;


        items.push(item);
        textItemId = items[totalNumberOfAllItems][0];
        textItemName = items[totalNumberOfAllItems][1];
        textItemPrice = items[totalNumberOfAllItems][2];
        textItemCategory = items[totalNumberOfAllItems][3];
        textItemDescription = items[totalNumberOfAllItems][4];
        textItemImage = items[totalNumberOfAllItems][5];
        gssst = items[totalNumberOfAllItems][6];


        createACardForItems(textItemId, textItemName, textItemPrice, textItemCategory, textItemDescription, textItemImage, gssst);
 
        
        

        totalNumberOfAllItems = totalNumberOfAllItems + 1;

    });


 }


 function changeCardBackground(iid, haveToChange) {
    let found=false;

    let elementCard = document.querySelector("div[name='"+iid+"']");

    if(totalOrders.length !== 0){

        for(let k = 0; k < totalOrders.length; k++){
    
            if(totalOrders[k][0] === iid){
                found = true;
                if(haveToChange == '1'){

                    // elementCard.style.borderColor = '#e1472b';
                    elementCard.style.borderColor = '#5856D6';
                } else {

                    elementCard.style.borderColor = '#fff';
                }
                
                break;
            }
        }

        if(!found){

            elementCard.style.borderColor = '#fff';
        }

    } else {

  
        elementCard.style.borderColor = '#fff';

    }





 }

 Array.prototype.remove = function(index) {
    this.splice(index, 1);
    
}


function getDataFromLocalStorage() {


    sessionData = JSON.parse(sessionStorage.getItem('totalOrders'));

    for(let i = 0; i<sessionData.length; i++){
        for(let y= 0; y< parseInt(sessionData[i][1]); y++){
            
            getItemIdFromCards(sessionData[i][0]+"", true);

        }
        
    }

}


function viewCardDetails(iiiiiid){

    let elementCard1 = document.querySelector("div[name='"+iiiiiid+"']");
   
    let oldOrderPresent = 0;
    sessionStorage.setItem('totalOrders', JSON.stringify(totalOrders));
    sessionStorage.setItem('clickedItem', iiiiiid);
    sessionStorage.setItem('phone', phoneNumber);

    for(let i = 0; i<totalOrders.length; i++){

        if(totalOrders[i][0] == iiiiiid){

            sessionStorage.setItem('totalNumber', totalOrders[i][1]);
            oldOrderPresent = 1;
            break;

        }
        else{
            oldOrderPresent = 0;
        }
        
    }



    if(oldOrderPresent == 0){
        sessionStorage.setItem('totalNumber', 'notSet');
    }

    window.open("./itemDetails.html", "_top");

    

    





}






 function getItemIdFromCards(buttonItemId, increase) {

    let order =[];
    let things;
    
    let isDone = false;
    let allTextElements = document.querySelector("h2[name='"+buttonItemId+"']");
    let allCardElements = document.getElementsByClassName("card");
    let itemPrice = document.querySelector("h2[name='"+buttonItemId+"_price']");
    let titleOfItem = document.querySelector("h1[name='"+buttonItemId+"_name']");
    let imageOfItem = document.querySelector("img[name='"+buttonItemId+"_image']");

    let gst_percentage1 = document.querySelector("h4[name='"+buttonItemId+"_gst']");
    
    



    if(increase){

        
    if(totalOrders.length !== 0){

        for(let k = 0; k < totalOrders.length; k++){
    
            if(totalOrders[k][0] === buttonItemId){
                things = parseInt(totalOrders[k][1]) + 1;
                totalOrders[k][1]  = things + '';
                allTextElements.innerText = totalOrders[k][1]+'';
                totalNumberOfSelection = totalNumberOfSelection+1;
                if(totalNumberOfSelection == 0){
                    orderooo.hidden = true;
                }else {
                    orderooo.hidden = false;
                }
            orderooo.innerText = totalNumberOfSelection+'';
                changeCardBackground(buttonItemId,'1');
                isDone = true;
                break;
            }
        }
    } 
    
        if(!isDone) {
            
            order[0] = buttonItemId;
            order[1] = 1+'';
            order[2] = itemPrice.innerText+'';
            order[3] = titleOfItem.innerText+'';
            order[4] = imageOfItem.src + '';
            order[5] = (((parseFloat(gst_percentage1.innerText))/100)*parseFloat(itemPrice.innerText))+'';

            totalOrders.push(order);
            allTextElements.innerText = totalOrders[totalOrders.length-1][1]+'';

            totalNumberOfSelection = totalNumberOfSelection+1;
            
            if(totalNumberOfSelection == 0){
                orderooo.hidden = true;
            }else {
                orderooo.hidden = false;
            }
            orderooo.innerText = totalNumberOfSelection+'';
            changeCardBackground(buttonItemId,'1');
            
            
    
     
    }
        
    } else {

        if(totalOrders.length !== 0){

            for(let k = 0; k < totalOrders.length; k++){
        
                if(totalOrders[k][0] === buttonItemId){
                    if(totalOrders[k][1]=='1'){
                        totalOrders.remove(k);
                        totalNumberOfSelection = totalNumberOfSelection-1;
                        if(totalNumberOfSelection == 0){
                            orderooo.hidden = true;
                        }else {
                            orderooo.hidden = false;
                        }
                        orderooo.innerText = totalNumberOfSelection+'';
                        changeCardBackground(buttonItemId, '0');
                        allTextElements.innerText = 0+'';
                        
                        
                    } else {
                        things = parseInt(totalOrders[k][1]) - 1;
                    totalOrders[k][1]  = things + '';
                    allTextElements.innerText = totalOrders[k][1]+'';
                    totalNumberOfSelection = totalNumberOfSelection-1;
                    if(totalNumberOfSelection == 0){
                        orderooo.hidden = true;
                    }else {
                        orderooo.hidden = false;
                    }
            orderooo.innerText = totalNumberOfSelection+'';
                    changeCardBackground(buttonItemId, '1');

                    }
                    
                    isDone = true;
                    break;
                }
            }
        }
        

        
            if(!isDone) {
                // 
                // order[0] = buttonItemId;
                // order[1] = 1+'';
                // totalOrders.push(order);
                // allTextElements.innerText = totalOrders[totalOrders.length-1][1]+'';
                // changeCardBackground(buttonItemId);
                
                
        
         
        }

    }




sessionStorage.setItem('totalOrders', JSON.stringify(totalOrders));

 }




async function createACardForItems(textItemId, textItemName, textItemPrice, textItemCategory, textItemDescription, textItemImage, gstAmount) {

    const cardCollection = document.getElementsByClassName('all_cards')[0];

    const mainCard = document.createElement('div');
    mainCard.className = 'card';
    mainCard.id = textItemId;
    mainCard.setAttribute('name', textItemId);
    


    const mainCardImage = document.createElement('div');
    mainCardImage.className = 'card-image';

    const mainImage = document.createElement('img');
    mainImage.className = 'image';
    let imageID = textItemId+'_image';
    mainImage.setAttribute('name', imageID);
    mainImage.src = await textItemImage;
    mainCardImage.appendChild(mainImage);
    mainCardImage.onclick = function () {
        
        viewCardDetails(mainCard.id);
     };


    const mainCardInfo = document.createElement('div');
    mainCardInfo.className = 'card-info';

    const mainItemName = document.createElement('h1');
    mainItemName.id = 'item_name';
    let titleID = textItemId+'_name';
    mainItemName.setAttribute('name', titleID);
    mainItemName.innerText = textItemName;
    mainCardInfo.appendChild(mainItemName);

    const mainCategoryName = document.createElement('p');
    mainCategoryName.id = 'category_name';
    if(textItemCategory == '0'){
        mainCategoryName.innerText = 'Today\'s Price: ';
        mainCategoryName.style.color = '#000';
        mainCategoryName.style.backgroundColor = "#fff";
        mainCategoryName.style.paddingLeft = 0;
    }else {
        mainCategoryName.innerText = textItemCategory;
    }
    
    mainCardInfo.appendChild(mainCategoryName);

    const mainItemDescription = document.createElement('p');
    mainItemDescription.id = 'item_description';
    if(textItemDescription == "0"){
        mainItemDescription.style.textDecoration = 'none';
        mainItemDescription.innerText = "Rs. ";
    }else {
        mainItemDescription.innerText = textItemDescription;
    }

    
    mainCardInfo.appendChild(mainItemDescription);


    const gstPercentage = document.createElement('h4');
    gstPercentage.id='gst_percentage';
    let gstID = textItemId+'_gst';
    gstPercentage.setAttribute('name', gstID);
    gstPercentage.innerText = gstAmount+'';
    gstPercentage.hidden= true;
    mainCardInfo.appendChild(gstPercentage);



    const mainItemPrice = document.createElement('h2');
    mainItemPrice.id = 'item_price';
    let texxtID = textItemId+'_price';
    mainItemPrice.setAttribute('name', texxtID);
    mainItemPrice.innerText = textItemPrice;
    mainCardInfo.appendChild(mainItemPrice);

    const mainBuyButton = document.createElement('button');
    mainBuyButton.className = 'buy-button';
    mainBuyButton.id = textItemId;
    mainBuyButton.innerText = '+';
    mainBuyButton.onclick = function () {
       
        getItemIdFromCards(mainBuyButton.id, true);
     };
    mainCardInfo.appendChild(mainBuyButton);

    const mainNumberOfItems = document.createElement('h2');
    mainNumberOfItems.className = 'number_of_items';
    mainNumberOfItems.setAttribute('name', textItemId);
    mainNumberOfItems.innerText = '0';
    mainCardInfo.appendChild(mainNumberOfItems);

    const mainViewButton = document.createElement('button');
    mainViewButton.className = 'view-button';
    mainViewButton.id = textItemId;
    mainViewButton.innerText = '-';

    mainViewButton.onclick = function () {
        
        getItemIdFromCards(mainBuyButton.id, false);
     };

     


    mainCardInfo.appendChild(mainViewButton);

    

    mainCard.appendChild(mainCardImage);
    mainCard.appendChild(mainCardInfo);


    

    cardCollection.appendChild(mainCard);


 }


const favDialog = document.getElementById('favDialog');
const outputBox = document.querySelector('output');
const confirmBtn = favDialog.querySelector('#confirmBtn');


// If a browser doesn't support the dialog, then hide the
// dialog contents by default.
if (typeof favDialog.showModal !== 'function') {
  favDialog.hidden = true;
  /* a fallback script to allow this dialog/form to function
     for legacy browsers that do not support <dialog>
     could be provided here.
  */
}
// "Update details" button opens the <dialog> modally
orderNow.addEventListener('click', () => {
  if (typeof favDialog.showModal === "function") {


    createCartList();

    favDialog.showModal();
  } else {
    outputBox.value = "Sorry, the <dialog> API is not supported by this browser.";
  }
});
// "Favorite animal" input sets the value of the submit button
// "Confirm" button of form triggers "close" on dialog because of [method="dialog"]
favDialog.addEventListener('close', () => {

  if(favDialog.returnValue === "cancel"){
    const element= document.getElementsByClassName('cartShow_all_list')[0];
    element.remove();
    const element1 = document.createElement('div'); 
    element1.className = 'cartShow_all_list';
    form.insertBefore(element1, price_details);

    favDialog.hidden = false;
  }
  else {
    if(totalOrders.length==0){
        alert("Please first select item.");
    }else {
        sessionStorage.setItem('totalorder', totalOrders);
        newWindowWork();
    }
    

  }



});



function detailsWindows() {









}

function createCartList(){

    let price_price_sub_total = document.getElementById('price_price_sub_total');
    let price_price_delivery_charges = document.getElementById('price_price_delivery_charges');
    let price_price_gst = document.getElementById('price_price_gst');
    let price_price_total_amount = document.getElementById('price_price_total_amount');




    let priceSubTotal =0.0;
    let priceGst = 0.0;

    for(let i = 0; i<totalOrders.length; i++){

        // createListInCart(itemName, price, quantity, gst, img);
        createListInCart(totalOrders[i][3], totalOrders[i][2], totalOrders[i][1], totalOrders[i][5], totalOrders[i][4]);
        priceGst = priceGst+ parseFloat(totalOrders[i][5]) * parseFloat(totalOrders[i][1]);
        priceSubTotal = (priceSubTotal)+(parseFloat(totalOrders[i][2])*parseFloat(totalOrders[i][1]));

    }

    price_price_sub_total.innerText = 'Rs. '+priceSubTotal.toFixed(2)+'';
    price_price_delivery_charges.innerText = 'Rs. '+deliveryCharges;
    price_price_gst.innerText = 'Rs. '+priceGst.toFixed(2);

    price_price_total_amount.innerText ='Rs. '+(priceGst+parseFloat(deliveryCharges)+priceSubTotal).toFixed(2);



    



}


function createListInCart(itemName, itemPrice, itemQuantity, itemGst, itemImg){

    const cartShow_all_list = document.getElementsByClassName('cartShow_all_list')[0];
    
    const cartshowlist = document.createElement('div');
    cartshowlist.id='cartShow_list';

    const cartShowListItemImage = document.createElement('img');
    cartShowListItemImage.id = 'cartShow_list_item_image';
    cartShowListItemImage.src = itemImg;
    cartshowlist.appendChild(cartShowListItemImage);

    const cartShowListItemName = document.createElement('p');
    cartShowListItemName.id = 'cartShow_list_item_name';
    cartShowListItemName.innerText = itemName;
    cartshowlist.appendChild(cartShowListItemName);

    const cartShowListItemNumber = document.createElement('p');
    cartShowListItemNumber.id = 'cartShow_list_item_number';
    cartShowListItemNumber.innerText = 'QNTY: '+itemQuantity;
    cartshowlist.appendChild(cartShowListItemNumber);

    const cartShowListItemPrice = document.createElement('p');
    cartShowListItemPrice.id = 'cartShow_list_item_price';
    cartShowListItemPrice.innerText = 'Rs. '+itemPrice;
    cartshowlist.appendChild(cartShowListItemPrice);


    cartShow_all_list.appendChild(cartshowlist);


}







function newWindowWork() {

    sessionStorage.setItem('totalOrdersFinal', JSON.stringify(totalOrders));
    sessionStorage.setItem('phoneNumberFinal', phoneNumber);
    sessionStorage.setItem('storeLinkFinal', storeLink);
    sessionStorage.setItem('deliveryChargesFinal', deliveryCharges);


    window.open("./insertDeliveryDetails.html", "_top");
    



}

function work(){


    
}







