function levelset = LinearSystem( time_step, levelset, Dphi, b,a) 
c = Dphi(:) ./ b(:); %|gradient u|/b ,setting b=g here
Blower = -a.*time_step .*  Dphi(:) .*  2./ (c + c( [end 1 : end-1])) ;
Bmain = 1 - a.*time_step .*  Dphi(:) .*( 2./( - c - c([end 1 : end-1]) ) + 2./(-c - c([2 : end 1])));%unit matrix only works for the main diag
Bupper = - a.*time_step .*  Dphi(:) .* 2./( c + c([2 : end 1]));
u = TDMA ( Bmain, Bupper(1 : end-1), Blower(2 : end), levelset( : )); %used to solve the diagonally dominant tridiagonal linear system
levelset = reshape( u, size(levelset)); 