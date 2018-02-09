if [ $# -eq 1 ]; then
    if [ $1 == "--windows" ]; then
        windows=true
    fi
fi

echo "#   Running the tests for each mutant..."
SCRIPT_PATH="$(cd "$(dirname "$0")" && pwd)" # Répertoire du script 'build_mutants.sh'

#Clean for old report
rm -f $SCRIPT_PATH/unsuccessful_tests.txt
rm -f $SCRIPT_PATH/report.html

#Add header html
echo "<!DOCTYPE html>
<html>
<head>
<meta charset='UTF-8'>
<title>Generate Report Mutant</title>
<style type='text/css'>
.tg  {border-collapse:collapse;border-spacing:0;border-color:#999;}
.tg td{font-family:Arial, sans-serif;font-size:14px;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#999;color:#444;background-color:#F7FDFA;}
.tg th{font-family:Arial, sans-serif;font-size:14px;font-weight:normal;padding:10px 5px;border-style:solid;border-width:1px;overflow:hidden;word-break:normal;border-color:#999;color:#fff;background-color:#26ADE4;}
.tg .tg-9hbo{font-weight:bold;vertical-align:top}
.tg .tg-yw4l{vertical-align:top}
body {
  padding-top: 50px;
}
.starter-template {
  padding: 10px 15px;
  text-align: center;
}
</style>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css' integrity='sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u' crossorigin='anonymous'>
<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css' integrity='sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp' crossorigin='anonymous'>
<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js' integrity='sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa' crossorigin='anonymous'></script>
</head>
<body>
<div class='container'>
<div class='starter-template'>
<h1>Mutant Generate Report</h1>" >> $SCRIPT_PATH/report.html

#Add Percentage mutation
echo "<h2>Percentage of mutation by mutant</h2><br/>" >> $SCRIPT_PATH/report.html
cat $SCRIPT_PATH/spoon-mutators/src/main/resources/percentages.properties >> $SCRIPT_PATH/report.html
echo "<br/><font color='red'>If the name of mutant doesn't appear the coverage of mutating is 100%</font><br/>" >> $SCRIPT_PATH/report.html
echo " " >> $SCRIPT_PATH/report.html


# For each mutated folder, we execute the test command
find $SCRIPT_PATH -name 'mutant_*' | while read line; do
  mvn test -q -f ${line}/pom.xml >> $SCRIPT_PATH/unsuccessful_tests.txt

  folder_name_only=$(basename ${line#*./})

  echo "Tests runned on $folder_name_only"


  echo "<br/>" >> $SCRIPT_PATH/report.html
  echo "<h3>Tests of '$folder_name_only'</h3>" >> $SCRIPT_PATH/report.html
  echo "<br/>" >> $SCRIPT_PATH/report.html

  #Analyze output test files
  DirectorySurefire=$SCRIPT_PATH/$folder_name_only/target\/surefire-reports

  if [ -d "$DirectorySurefire" ]
  then
    NumberRunTest=0
    NumberFailuresTest=0
    NumberErrorTest=0
    NumberSkippedTest=0
    #Parse each file for extract specific information number
    find $DirectorySurefire -name '*.txt' | while read fileTest; do
      typeset -i variableNumberRunTest=$(sed -n 's:.*run\: \(.*\), Failures.*:\1:p' ${fileTest})
      NumberRunTest=$(( $NumberRunTest + $variableNumberRunTest ))
      echo $NumberRunTest > $SCRIPT_PATH/NumberRunTesttmp.txt

      typeset -i variableNumberFailuresTest=$(sed -n 's:.*Failures\: \(.*\), Errors.*:\1:p' ${fileTest})
      NumberFailuresTest=$(( $NumberFailuresTest + $variableNumberFailuresTest ))
      echo $NumberFailuresTest > $SCRIPT_PATH/NumberFailuresTesttmp.txt

      typeset -i variableNumberErrorsTest=$(sed -n 's:.*Errors\: \(.*\), Skipped.*:\1:p' ${fileTest})
      NumberErrorTest=$(( $NumberErrorTest + $variableNumberErrorsTest ))
      echo $NumberErrorTest > $SCRIPT_PATH/NumberErrorTesttmp.txt

      typeset -i variableNumberSkippedTest=$(sed -n 's:.*Skipped\: \(.*\), Time.*:\1:p' ${fileTest})
      NumberSkippedTest=$(( $NumberSkippedTest + $variableNumberSkippedTest ))
      echo $NumberSkippedTest > $SCRIPT_PATH/NumberSkippedTesttmp.txt

    done

    #Add number in the report
    typeset -i variableNumberRunTest=$(cat $SCRIPT_PATH/NumberRunTesttmp.txt)
    typeset -i variableNumberFailuresTest=$(cat $SCRIPT_PATH/NumberFailuresTesttmp.txt)
    typeset -i variableNumberErrorsTest=$(cat $SCRIPT_PATH/NumberErrorTesttmp.txt)
    typeset -i variableNumberSkippedTest=$(cat $SCRIPT_PATH/NumberSkippedTesttmp.txt)

    #Calculating percentage
    NumberFailedTest=$(( $variableNumberFailuresTest + $variableNumberErrorsTest + $variableNumberSkippedTest))
    variableTPourcentage=100
    pourcentage=$((($variableNumberRunTest - $NumberFailedTest) * $variableTPourcentage / $variableNumberRunTest))

    #Add table with statistic test values
    echo "<center><table class='tg'>
      <tr>
        <th class='tg-9hbo'>Tests</th>
        <th class='tg-9hbo'>Errors</th>
        <th class='tg-9hbo'>Failures</th>
        <th class='tg-9hbo'>Skipped</th>
        <th class='tg-9hbo'>SucessRate</th>
      </tr>
      <tr>
        <td class='tg-yw4l'>$variableNumberRunTest</td>
        <td class='tg-yw4l'>$variableNumberErrorsTest</td>
        <td class='tg-yw4l'>$variableNumberFailuresTest</td>
        <td class='tg-yw4l'>$variableNumberSkippedTest</td>
        <td class='tg-yw4l'>$pourcentage %</td>
      </tr>
    </table></center>" >> $SCRIPT_PATH/report.html


    #Generate html page of surefire report
    mvn surefire-report:report-only -q -f ${line}/pom.xml >> $SCRIPT_PATH/unsuccessful_tests.txt
    mvn site -DgenerateReports=false -q -f ${line}/pom.xml >> $SCRIPT_PATH/unsuccessful_tests.txt

    #Add link of html page in report
    if [ "$windows" == "true" ]; then
        # Remove the first escaped '/' which is '\/'
	    NEW_PROJECT_PATH=$(echo $SCRIPT_PATH | sed 's,^\/,,')
	    # replace all escaped '/' which are '\/' by escaped '\\' which is '\\\\'
        NEW_PROJECT_PATH=$(echo $NEW_PROJECT_PATH | sed 's,\/,\\,g')
        # replace first letter which is drive letter ('d' or 'c') by the same letter suffixed with ':'
        NEW_PROJECT_PATH=$(echo $NEW_PROJECT_PATH | sed 's,\([A-z]\),\1:,')
        echo "<br/><a href='file:///$NEW_PROJECT_PATH/$folder_name_only/target/site/surefire-report.html' target='_blank'>Rapport test de $folder_name_only</a><br/>" >> $SCRIPT_PATH/report.html
    else
        echo "<br/><a href='file://$SCRIPT_PATH/$folder_name_only/target/site/surefire-report.html' target='_blank'>Rapport test de $folder_name_only</a><br/>" >> $SCRIPT_PATH/report.html
    fi
    echo " " >> $SCRIPT_PATH/report.html
    echo " " >> $SCRIPT_PATH/report.html
  else
    #Test can't run
    echo "<h4><font color='red'>Broken Test</font></h4><br/>" >> $SCRIPT_PATH/report.html
  fi
done
echo "</div></div></body>
</html>" >> $SCRIPT_PATH/report.html
#Clean folder
rm -f $SCRIPT_PATH/*tmp.txt
rm -f $SCRIPT_PATH/unsuccessful_tests.txt
echo "#   Tests has been runned on all mutants"
