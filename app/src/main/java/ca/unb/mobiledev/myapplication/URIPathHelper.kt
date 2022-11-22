package ca.unb.mobiledev.myapplication

import android.net.Uri
import android.widget.Toast


class URIPathHelper {
   // private fun uploadProfileCoverPhoto(uri: Uri) {
        //pd.show()

        // We are taking the filepath as storagepath + firebaseauth.getUid()+".png"
        //val filepathname: String =
          //  storagepath.toString() + "" + profileOrCoverPhoto + "_" + firebaseUser.getUid()
       // val storageReference1: StorageReference = storageReference.child(filepathname)
        //storageReference1.putFile(uri)
            //.addOnSuccessListener(object : OnSuccessListener<UploadTask.TaskSnapshot?>() {
               // fun onSuccess(taskSnapshot: UploadTask.TaskSnapshot) {
                 //   val uriTask: Task<Uri> = taskSnapshot.getStorage().getDownloadUrl()
                   // while (!uriTask.isSuccessful());

                    // We will get the url of our image using uritask
                    //val downloadUri: Uri = uriTask.getResult()
                    //if (uriTask.isSuccessful()) {

                        // updating our image url into the realtime database
                      //  val hashMap: HashMap<String, Any> = HashMap()
                        //hashMap[profileOrCoverPhoto] = downloadUri.toString()
                        //databaseReference.child(firebaseUser.getUid()).updateChildren(hashMap)
                          //  .addOnSuccessListener(object : OnSuccessListener<Void?>() {
                                //fun onSuccess(aVoid: Void?) {
                            //        pd.dismiss()
                                  //  Toast.makeText(
                              //          this@EditProfilePage,
          //                              "Updated",
            //                            Toast.LENGTH_LONG
                                    //).show()
                          //      }
                            //}).addOnFailureListener(object : OnFailureListener() {
              //                  fun onFailure(e: Exception) {
                              //      pd.dismiss()
                //                    Toast.makeText(
                                //        this@EditProfilePage,
                  //                      "Error Updating ",
                    //                    Toast.LENGTH_LONG
                      //              ).show()
                        //        }
                           // })
                   // } else {
                     //   pd.dismiss()
                       // Toast.makeText(this@EditProfilePage, "Error", Toast.LENGTH_LONG).show()
                   // }
                //}
            //}).addOnFailureListener(object : OnFailureListener() {
              //  fun onFailure(e: Exception) {
                //    pd.dismiss()
                  //  Toast.makeText(this@EditProfilePage, "Error", Toast.LENGTH_LONG).show()
               // }
            //})
    //}
}