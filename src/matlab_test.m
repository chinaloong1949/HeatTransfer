wholeMatrix=load('整体矩阵修正后.txt');
b=wholeMatrix(:,271);
k=wholeMatrix(:,1:270);
result=k\b
