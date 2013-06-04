load TrainingDataSet;

temp = size(JueJuTrainBad);
JueJuBadTag = ones(temp(1),1);
JueJuTrainBad2 = [JueJuTrainBad,JueJuBadTag];

temp = size(JueJuTrainGood);
JueJuGoodTag = 2*ones(temp(1),1);
JueJuTrainGood2 = [JueJuTrainGood,JueJuGoodTag];

temp = size(LvShiTrainBad);
LvShiBadTag = ones(temp(1),1);
LvShiTrainBad2 = [LvShiTrainBad,LvShiBadTag];

temp = size(LvShiTrainGood);
LvShiGoodTag = 2*ones(temp(1),1);
LvShiTrainGood2 = [LvShiTrainGood,LvShiGoodTag];

MegaMatrix = [JueJuTrainBad2;JueJuTrainGood2;LvShiTrainBad2;LvShiTrainGood2];


%annova analysis
%name the different matrices:
GoodBadTag = MegaMatrix(:,6);
struct =   MegaMatrix(:,1);
emotion = MegaMatrix(:,2);
condense =MegaMatrix(:,3); 
rhyme = MegaMatrix(:,4);
rhymeType =MegaMatrix(:,5);

p = anovan(GoodBadTag,{struct,emotion condense rhyme rhymeType});

%SVM

%Linear:
Training = [JueJuTrainBad;JueJuTrainGood;LvShiTrainBad;LvShiTrainGood];
Group = [JueJuBadTag;JueJuGoodTag;LvShiBadTag;LvShiGoodTag];
svmStruct = svmtrain(Training,Group);

%multilayer



Sample = Training(20,:);
newGroup = svmclassify(svmStruct,Sample);