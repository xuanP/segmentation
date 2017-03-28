function out =readImage(a)
%read an image and transfer it to a grey one
I=imread(a);%read an image
[d1,d2,d3] = size(I); 
if(d3 > 1) 
I = rgb2gray(I);%if it's a gray image then don't need to change it
end 
I = double(I) / 255; 
I1 = uint8(255 * I * 0.5 + 0.5); %change the double to unit8

imshow(I1);%show the image
axis([0,d2,0,d1]);
axis on;
imwrite(I1,'grey.jpg');%write the grey image into a new file
out = I1;

