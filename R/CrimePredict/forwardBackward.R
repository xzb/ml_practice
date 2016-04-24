data=read.csv(file.choose(),header=FALSE)
V11=ifelse(data$V11=="g",0,1)
data1=data[,-11]
data2=data.frame(data1,V11=c(V11))
attach(data2)
# USING FORWARD SUBSET SELECTION
regfit.fwd=regsubsets(V11~.,data2,nvmax=10,method="forward")
summary(regfit.fwd)
reg.summary=summary(regfit.fwd)
par(mfrow = c(2,2))
plot(reg.summary$rss,xlab="Number of Variables",ylab="RSS",type="l")
plot(reg.summary$adjr2,xlab="Number of Variables",ylab="Adjusted RSq",type="l")
plot(reg.summary$cp,xlab="Number of Variables",ylab="Cp",type='l')
plot(reg.summary$bic,xlab="Number of Variables",ylab="BIC",type='l')
which.min(reg.summary$rss)
#10
which.max(reg.summary$adjr2)
#9
which.min(reg.summary$cp)
#8
which.min(reg.summary$bic)
#6
k=10
set.seed(1)
folds=sample(1:k,nrow(data2),replace=TRUE)
cv.errors=matrix(NA,k,10, dimnames=list(NULL, paste(1:10)))

predict.regsubsets = function(object, newdata, id, ...){
  forms = as.formula(object$call[[2]])
  mat = model.matrix(forms, newdata)
  coefi = coef(object, id = id)
  xvars = names(coefi)
  mat[,xvars] %*% coefi
}

for(j in 1:10){
  fwd.fit=regsubsets(V11~., data=data2[folds!=j,],nvmax=10,method="forward")
  for(i in 1:10){
    pred=predict(fwd.fit,data2[folds==j,],id=i)
    cv.errors[j,i]=mean((data2$V11[folds==j]-pred)^2)
  }
}
mean.cv.errors=apply(cv.errors,2,mean)
mean.cv.errors
plot(mean.cv.errors,type='b')
which.min(mean.cv.errors)
#9
coef(fwd.fit,9)
# except V8
# USING BACKWARD SUBSET SELECTION
regfit.bwd=regsubsets(V11~.,data=data2,nvmax=10,method="backward")
reg.summary.b=summary(regfit.bwd)
par(mfrow = c(2,2))
plot(reg.summary.b$rss,xlab="Number of Variables",ylab="RSS",type="l")
plot(reg.summary.b$adjr2,xlab="Number of Variables",ylab="Adjusted RSq",type="l")
plot(reg.summary.b$cp,xlab="Number of Variables",ylab="Cp",type='l')
plot(reg.summary.b$bic,xlab="Number of Variables",ylab="BIC",type='l')
which.min(reg.summary.b$rss)
#10
which.max(reg.summary.b$adjr2)
#9
which.min(reg.summary.b$bic)
#6
which.min(reg.summary$cp)
#8
for(j in 1:10){
  bwd.fit=regsubsets(V11~., data=data2[folds!=j,],nvmax=10,method="backward")
  for(i in 1:10){
    pred=predict(bwd.fit,data2[folds==j,],id=i)
    cv.errors[j,i]=mean((data2$V11[folds==j]-pred)^2)
  }
}
mean.cv.errors=apply(cv.errors,2,mean)
mean.cv.errors
plot(mean.cv.errors,type='b')
which.min(mean.cv.errors)
#9
coef(bwd.fit,9)
