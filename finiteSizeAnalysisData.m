map = [
  8, 5.4;
  16, 1.98;
  32, 1.45;
  64, 1.28;
  128, 1.22;
  256, 1.19;
  512, 1.185;
  ];

xs = 1 ./ map(:,1);

clf;

figure(1);
plot(xs,map(:,2),'*')
hold on;

xs2 = linspace(-0.05,0.15,100);
ys = exp(20*xs2.^0.72-3) + 1.15;
plot(xs2,ys)
hold on;

ys2 = -1 ./ (50*xs2-1) + 2;
%plot(xs2,ys2);
hold on;

axis([-0.05 0.15 0 7]);