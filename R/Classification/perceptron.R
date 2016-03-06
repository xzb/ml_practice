# install.packages("neuralnet")
library(neuralnet)

createModel <- function (trainingData)
{
  col = ncol(trainingData)
  n <- colnames(trainingData)
  formula <- as.formula(paste(n[col], "~", paste(n[1 : col-1], collapse = " + ")))    # last col as class
  
  model <- neuralnet(formula, data=trainingData, hidden = 0, linear.output = F)
}


findAccuracy <- function (model, testData)
{
  col = ncol(testData)

  predictResult <- compute(model, testData[, 1 : col-1])
  
  #errorRate <- sum( (predictResult$net.result - testData[, col])^2 ) / nrow(testData)
  absError <- sum( abs(predictResult$net.result - testData[, col]) ) / nrow(testData)
  accuracy <- 1 - absError
}