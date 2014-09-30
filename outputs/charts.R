# Generating charts to JLAMP

# Load time data set
outTimes <- read.csv("~/Documents/mmatching/outputs/outTimes.txt", header=FALSE)
outTimes <- as.numeric(as.character(outTimes))

# Load ResponseTime
outRT <- read.csv("~/Documents/mmatching/outputs/outRT.txt", header=FALSE)
outRT <- as.numeric(as.character(outRT))

# Load Throughput
outTHP <- read.csv("~/Documents/mmatching/outputs/outTHP.txt", header=FALSE)
# fraction to decimal
outTHP <- sapply(t(outTHP), function(x) eval(parse(text=x)))


# Load Failures
outFailures <- read.csv("~/Documents/mmatching/outputs/outFailures.txt", header=FALSE)
outFailures <- sapply(t(outFailures), function(x) eval(parse(text=x)))

# Response Time chart
rtAsymp <- rep(40, length(outTimes))
plot(outTimes, outRT, type="o", ylab = "Response Time (time units)", pch=20,
     xlab = "Time (time units)")
lines(outTimes, rtAsymp, col="green", lty=4)
grid(nx = 10, ny = 4, col = "gray", lty = "dotted",
     lwd = par("lwd"), equilogs = TRUE)
title("Response Time Observer")


# Throughput chart
thpAsymp <- rep(0.025, length(outTimes))
plot(outTimes, outTHP, type="o", ylab = "Throughput (pieces/time units)", pch=20,
     xlab = "Time (time units)")
lines(outTimes, thpAsymp, col="green", lty=4)
grid(nx = 10, ny = 4, col = "gray", lty = "dotted",
     lwd = par("lwd"), equilogs = TRUE)
title("Throughput Observer")

# FailureRate chart
plot(outTimes, outFailures, type="o", ylab = "Failure rate (pieces/time units)", pch=20,
     xlab = "Time (time units)")
lines(outTimes, thpAsymp, col="green", lty=4)
grid(nx = 10, ny = 4, col = "gray", lty = "dotted",
     lwd = par("lwd"), equilogs = TRUE)
title("Throughput Observer")
