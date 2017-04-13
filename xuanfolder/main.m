%this is the main program that you can run directly in matlab.
%Other files are just functions
clear
close all;
img=readImage('images.jpg');
sigma=1.2; %parameter in Gaussien Kernel
g=stopFunction(img,1,sigma);

%initial LSF
LSF=Ini_square(img,5);
figure();
%imagesc(img);=>to show a scalable image.
%colormap(gray);
imshow(img);
hold on;
[c,h]=contour(LSF,[0 0],'r');
title('initial contour');
%level set begins
levelset=AOS(img, LSF, -0.3,0.7, 600 , g);
