function g = stopFunction( u, k , sig) 
mask = fspecial( 'gaussian', 3, sig);
u = conv2( u, double( mask ), 'same'); 
%according to the article formule(14), here we do a Gaussian kernel of standard deviation u.

[ ux,uy ] = gradient(u); 
 g = 1 ./ (1 + k * (ux .^ 2 +  uy .^ 2));