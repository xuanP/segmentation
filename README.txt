Status:The 2nd basic model(explicit one) is not uploaded yet.

To complete the Fast Implicit Contour Model, the original article suggest use AOS to improve both the implicit active contour models and the explicit active contour models


After searching for some information, for implicit active contour models, I found that DRLSE level-set-based model is very suitable for being the basic model.
* About DRLSE :
	Title: Distance Regularized Level Set Evolution and Its Application to Image Segmentation
	Author: Chunming Li, Chenyang Xu, Senior Member, IEEE, Changfeng Gui, and Martin D. Fox, Member, IEEE
	IEEE TRANSACTIONS ON IMAGE PROCESSING, VOL. 19, NO. 12, DECEMBER 2010

* Attention that logTransformation.m is not useful until now because I found when using it, the segmentation is even worse
* easy_seg.m is the main file to run, others are just functions. Easy_seg.m can do basic segmentation now.