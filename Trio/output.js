function getMain(){var rtnWin = app.mainWindow();return rtnWin;}
function dump(curr){curr.logElementTree();UIALogger.logMessage(curr);}
function wait(timer){if (timer >= 0){UIALogger.logMessage("Waiting for " + timer + " second(s).");target.delay(timer);}else{UIALogger.logWarning("negative wait");target.delay(1);}}
function elementVisible(element, wait, freq){if (freq == null){freq = 1;}if (wait == null){wait = 5;}var step = wait/freq;for (var i=0; i<step; i++){if (element.isVisible()){return true;}wait(freq);}element.logElement();throw("Element Not Visible");return false;}
//This is an iOS automation test.
UIALogger.logMessage("Test starting");
var target = UIATarget.localTarget();
var app = target.frontMostApp();
var home_window = getMain();
UIALogger.logMessage("Account Creation starting");
var timestamp = "2011-12-16 14:46:24.08"
var mv = getMain();
mv.tabBar().buttons()["Accounts"].tap();
wait(1);
var mv = getMain();
mv.navigationBars()[0].buttons()[1].tap();
wait(1);
var mv = getMain();
mv.tableViews()[0].cells()["Account Name"].textFields()[0].setValue("test account");
wait(1);
var mv = getMain();
mv.navigationBars()[0].buttons()["Save"].tap();
wait(1);
dump(mv);
var mv = getMain();
mv.tableViews()[0].cells()["test account"].scrollToElementWithName("test account");
if((mv.tableViews()[0].cells()["test account"].scrollToElementWithName("test account")).isVisible()){UIALogger.logPass("Assertion Passed");}else{UIALogger.logFail("Assertion Failed");}
var mv = getMain();
mv.tableViews()[0].cells()["Uniform Allies"].scrollToElementWithName("Uniform Allies");
if((mv.tableViews()[0].cells()["Uniform Allies"].scrollToElementWithName("Uniform Allies")).isVisible()){UIALogger.logPass("Assertion Passed");}else{UIALogger.logFail("Assertion Failed");}
var mv = getMain();
mv.tableViews()[0].cells()["test account"].tap();
wait(1);
var mv = getMain();
mv.tapWithOptions({x:0.4, y:0.5});
var mv = getMain();
mv.navigationBars()[1].buttons()[1].tap();
wait(1);
var mv = getMain();
mv.navigationBars()["Tickets"].buttons()[1].tap();
wait(1);
var mv = getMain();
mv.tableViews()[0].cells()[1].textFields()[0].setValue("test case");
wait(1);
var mv = getMain();
mv.navigationBars()[0].buttons()["Save"].tap();
wait(1);
var mv = getMain();
mv.tableViews()[0].cells()["test account"].scrollToElementWithName("test account");
if((mv.tableViews()[0].cells()["test account"].scrollToElementWithName("test account")).isVisible()){UIALogger.logPass("Assertion Passed");}else{UIALogger.logFail("Assertion Failed");}
var mv = getMain();
mv.tableViews()[0].cells()["Uniform Allies"].tap();
wait(1);
var mv = getMain();
mv.tableViews()[0].cells()["test account"].tap();
wait(1);
var mv = getMain();
mv.navigationBars()["test account"].buttons()["Edit"].tap();
wait(1);
var mv = getMain();
mv.tableViews()[1].scrollToElementWithName("Remove Account");
wait(1);
var mv = getMain();
mv.tableViews()[1].scrollDown();
wait(1);
var mv = getMain();
mv.tableViews()[1].cells()["Remove Account"].buttons()["Remove Account"].tap();
wait(1);
var mv = getMain();
mv.tapWithOptions({x:0.5, y:0.5});
UIALogger.logMessage("Done");
