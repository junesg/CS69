
cd /Users/JuneSG/CS69;
clear all;
clc;


R5wordsBadLvShi = dlmread('ResultsFor5wordsBadLvShi');
temp = size(R5wordsBadLvShi);
rows = temp(1)/6;
for i= 1:1:rows
    for j = 1:1:5
        LvShiTrainBad(i,j) = R5wordsBadLvShi((i-1)*6+j+1,1);
    end
end





R5wordsBadPoem = dlmread('resultsfor5wordsBadPoem');
temp = size(R5wordsBadPoem);
rows = temp(1)/6;
for i= 1:1:rows
    for j = 1:1:5
        JueJuTrainBad(i,j) = R5wordsBadPoem((i-1)*6+j+1,1);
    end
end




%For Jueju, it has 7 scores
%ID, pair with structural scors*2, emotional scores, condense, rhyme score, rhyme type
R5wordsJueJu = dlmread('resultsfor5wordsJueju');
temp = size(R5wordsJueJu);
rows = temp(1)/6;
for i= 1:1:rows
    for j = 1:1:5
        JueJuTrainGood(i,j) = R5wordsJueJu((i-1)*6+j+1,1);
    end
end


%For Lvshi, it has 9 scores
%ID, pair with structural scors*4, emotional scores, condense, rhyme score, rhyme type
R5wordsLvShi = dlmread('resultsfor5wordsLvShi');
temp = size(R5wordsLvShi);
rows = temp(1)/6;
for i= 1:1:rows
    for j = 1:1:5
        LvShiTrainGood(i,j) = R5wordsLvShi((i-1)*6+j+1,1);
    end
end

save('TrainingDataSet.mat','R5wordsBadPoem','JueJuTrainBad','R5wordsBadLvShi','R5wordsJueJu',...
    'R5wordsLvShi','LvShiTrainGood','JueJuTrainGood','LvShiTrainBad');

