# ASL Assist Android Mobile App

_ASL Assist (American Sign Language Assistant)_ is an Android mobile application that utilizes machine learning and computer vision written in Python to let users use their personal phone camera to recognize and communicate the American sign language alphabet. 

Table of Contents
=================
<!--ts-->
  * [ASL Assist Android Mobile App](#asl-assist-android-mobile-app)
  * [Table of Contents](#table-of-contents)
  * [Installation](#installation)
    * [Installing the Python Model](#installing-the-python-model)
    * [Installing the Android App](#installing-the-android-mobile-app)
    * [(Optional) Insert Your Own Model](#optional-insert-your-own-tflite-model)
  * [Mobile Application Reference](#mobile-application-reference)
  * [Machine Learning Model Reference](#machine-learning-model-reference)
  * [Meet the Authors](#meet-the-authors)
<!--te-->

Installation
============

Installing the Python Model
---

To get started, let's clone the Python model [repository](https://github.com/jbentleyh/ASL-Machine-Learning).

The Python model repository comes packed with many different useful features. The repository allows you to create, train, and test a machine learning model from scratch or easily import a pre-existing model. The repository also allows you to generate your own dataset or import a pre-existing dataset. 

To clone repository: 

```
git clone https://github.com/jbentleyh/ASL-Machine-Learning
```

To install dependencies: 
```
pip install -r requirements.txt
```


Please see the [readme](https://github.com/jbentleyh/Sign_Language_ML/blob/master/README.md) for more instructions on creating, training, and testing a machine learning model.

_Note: If using your own model, you need to [convert](https://www.tensorflow.org/lite/convert) your model to `.tflite` before installing the mobile application._

Installing the Android Mobile App
---------------------------------
To get started, let's clone the application [repository](https://github.com/jbentleyh/SignLangML_Android_Mobile_App).

To clone repository: 

```
git clone https://github.com/jbentleyh/ASL-Assist-Android-Mobile-App.git
```

Next, open [Android Studio](https://developer.android.com/studio). 

(Optional) Insert Your own `.tflite` Model
------------------------------------------
From the project source navigate:

```
SignLangML > app > assets
```
In this folder, place your `.tflite` model file.

<br/>

To open the cloned repository, navigate `File > Open Project`. Find the working folder and select the cloned repository as the project.

Once the project is open and synced. Click the build hammer as seen below.

![Untitled](https://user-images.githubusercontent.com/45768739/80325529-2c6b6c00-87fb-11ea-8c77-386567e2a0e5.png)

Once the project has successfully built, run the app.

![Untitled1](https://user-images.githubusercontent.com/45768739/80325726-eebb1300-87fb-11ea-8cc0-ee8518394b4c.png)

<br/>
<br/>

Mobile Application Reference
============================
![tfl](https://user-images.githubusercontent.com/45768739/80432979-48cedd80-88bb-11ea-9954-d4c83c5aad69.png)

ASL Assist uses [TensorFlow Lite](https://www.tensorflow.org/lite/) for Android. TensorFlow Lite is used to bridge the gap between the machine learning model and the mobile application. 

For more information, see the TensorFlow Lite Android [quickstart](https://www.tensorflow.org/lite/guide/android) guide.

![opencv](https://user-images.githubusercontent.com/45768739/80433735-51c0ae80-88bd-11ea-87e8-3a817d66ccff.png)

ASL Assist uses [OpenCV 3.4.8](https://opencv.org/releases/) for Android. OpenCV is used to open and control the user's phone camera for frame analysis from the machine learning model.

For more information on OpenCV, see the [documentation](https://docs.opencv.org/4.3.0/).

<br/>

Mobile application [source code](https://github.com/jbentleyh/ASL-Assist-Android-Mobile-App).

Machine Learning Model Reference
================================
![tfkeras](https://user-images.githubusercontent.com/45768739/80434838-7ec29080-88c0-11ea-925a-9ea0aa53ca94.png)

ASL uses [TensorFlow + Keras](https://www.tensorflow.org/guide/keras/overview) to power a Keras [Sequential](https://keras.io/getting-started/sequential-model-guide/) model. TensorFlow and Keras interface seemlessly, allowing for quick iteration, training, and testing models.

For more information on TensorFlow + Keras for Python, see the official [documentation](https://www.tensorflow.org/guide/keras/overview). For more information on the Keras Sequential model, see the Keras [documentation](https://keras.io/models/sequential/).

<br/>

Machine learning model [source code](https://github.com/jbentleyh/ASL-Machine-Learning).

Meet The Authors
================
Jeffrey Files     [GitHub](https://github.com/jjfiles)

Jansen Howell     [GitHub](https://github.com/jbentleyh)

Joseph Daughdrill [GitHub](https://github.com/jdaughd2)

[Documentation](https://jbentleyh.github.io/ASL-Assist-Docs/)

