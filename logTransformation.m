%function out = logTransformation(a)
%close all;  
%clear all;  
  
% -------------Log Transformations-----------------  
f = imread('grey.jpg');  
f = mat2gray(f,[0 255]);  
  
v = 10;  
g_1 = log2(1 + v*f)/log2(v+1);  
  
v = 30;  
g_2 = log2(1 + v*f)/log2(v+1);  
  
v = 200;  
g_3 = log2(1 + v*f)/log2(v+1);  
  
figure();  
subplot(1,2,1);  
imshow(f,[0 1]);  
xlabel('a).Original Image');  
%subplot(1,2,2);  
%imshow(g_1,[0 1]);  
%xlabel('b).Log Transformations v=10');  
  
%figure();  
subplot(1,2,2);  
imshow(g_2,[0 1]);  
xlabel('c).Log Transformations v=100');  
  
%subplot(1,2,2);  
%imshow(g_3,[0 1]);  
%xlabel('d).Log Transformations v=200'); 
out=g_2;