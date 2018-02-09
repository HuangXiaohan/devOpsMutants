if [ $# -lt 1 ]; then
	echo "Usage : execution <folder> [--windows]"
  echo "<folder> should be a path to a Maven project to which we would like to apply all our mutators"
  echo "[--windows] if you are on windows and emulating bash, you need to use this argument to use DOS path"
	exit 1
fi

project_folder=${1%/}

if [ $# -eq 2 ]; then
    if [ "$2" == "--windows" ]; then
        windows=true
    fi
fi


SCRIPT_PATH="$(cd "$(dirname "$0")" && pwd)" # #Chemin du script
SCRIPT_PATH=${SCRIPT_PATH//\//\\/} #Chemin du script avec les échappement
SCRIPT_PATH2="$(cd "$(dirname "$0")" && pwd)" # #Chemin du script


echo "#   Building the mutators..."
mvn -q -f $SCRIPT_PATH2/spoon-mutators/pom.xml clean install
echo "#   Mutators has been built"

echo "#   Building the mutants..."
find $SCRIPT_PATH2/spoon-mutators -name '*Mutator.java' | while read line; do
	mutator=$(basename ${line})
	mutator=${mutator%.java}

	mkdir $SCRIPT_PATH2/mutant_${mutator}
	cp ${project_folder}/pom.xml $SCRIPT_PATH2/mutant_${mutator}

	cd $SCRIPT_PATH2/mutant_${mutator}

	PROJECT_PATH=${project_folder//\//\\/}
	if [ "$windows" == "true" ]; then
        # Remove the first escaped '/' which is '\/'
	    NEW_PROJECT_PATH=$(echo $PROJECT_PATH | sed 's,^\\\/,,')
	    # replace all escaped '/' which are '\/' by escaped '\\' which is '\\\\'
        NEW_PROJECT_PATH=$(echo $NEW_PROJECT_PATH | sed 's,/,\\\\,g')
        # replace first letter which is drive letter ('d' or 'c') by the same letter suffixed with ':'
        NEW_PROJECT_PATH=$(echo $NEW_PROJECT_PATH | sed 's,\([A-z]\),\1:,')
	    CONTENT1="<testSourceDirectory>$NEW_PROJECT_PATH\\\\src\\\\test\\\\<\/testSourceDirectory>"
    else
        CONTENT1="<testSourceDirectory>$PROJECT_PATH\/src\/test\/<\/testSourceDirectory>"
    fi
	sed "s/<build>/<build>$CONTENT1/g" $SCRIPT_PATH2/mutant_${mutator}/pom.xml > $SCRIPT_PATH2/mutant_${mutator}/pom2.xml
	mv $SCRIPT_PATH2/mutant_${mutator}/pom2.xml $SCRIPT_PATH2/mutant_${mutator}/pom.xml

	cd $SCRIPT_PATH2

	cp ${project_folder}/pom.xml ${project_folder}/pom.xml.temp

	cd ${project_folder}
	CONTENT2="<dependency><groupId>devops.five<\/groupId><artifactId>mutators<\/artifactId><version>1.0-SNAPSHOT<\/version><scope>compile<\/scope><\/dependency>"
	if [ "$windows" == "true" ]; then
	    # Remove the first escaped '/' which is '\/'
	    OUTPUT_PATH=$(echo "$SCRIPT_PATH" | sed 's/^\\\///')
	    # replace all escaped '/' which are '\/' by escaped '\\' which is '\\\\'
	    OUTPUT_PATH=$(echo "$OUTPUT_PATH" | sed 's/\([A-z]\)/\1:/')
	    # replace first letter which is drive letter ('d' or 'c') by the same letter suffixed with ':'
	    OUTPUT_PATH=$(echo "$OUTPUT_PATH" | sed 's/\\\//\\\\/g')
	    CONTENT3="<plugin><groupId>fr.inria.gforge.spoon<\/groupId><artifactId>spoon-maven-plugin<\/artifactId><version>2.4.1<\/version><configuration><processors><processor>devops.five.mutators.${mutator}<\/processor><\/processors><outFolder>$OUTPUT_PATH\\\\mutant_${mutator}\\\\src\\\\main\\\\java<\/outFolder><\/configuration><executions><execution><phase>generate-sources<\/phase><goals><goal>generate<\/goal><\/goals><\/execution><\/executions><\/plugin>"
    else
	    CONTENT3="<plugin><groupId>fr.inria.gforge.spoon<\/groupId><artifactId>spoon-maven-plugin<\/artifactId><version>2.4.1<\/version><configuration><processors><processor>devops.five.mutators.${mutator}<\/processor><\/processors><outFolder>$SCRIPT_PATH\/mutant_${mutator}\/src\/main\/java<\/outFolder><\/configuration><executions><execution><phase>generate-sources<\/phase><goals><goal>generate<\/goal><\/goals><\/execution><\/executions><\/plugin>"
    fi
  CONTENT4="<plugin><groupId>org.apache.maven.plugins<\/groupId><artifactId>maven-surefire-plugin<\/artifactId><version>2.19.1<\/version><\/plugin>"

	sed "s/<dependencies>/<dependencies>$CONTENT2/g" ${project_folder}/pom.xml > ${project_folder}/pom2.xml
	mv ${project_folder}/pom2.xml ${project_folder}/pom.xml
	sed "s/<plugins>/<plugins>$CONTENT3/g" ${project_folder}/pom.xml > ${project_folder}/pom2.xml
	mv ${project_folder}/pom2.xml ${project_folder}/pom.xml
	sed "s/<plugins>/<plugins>$CONTENT4/g" ${project_folder}/pom.xml > ${project_folder}/pom2.xml
	mv ${project_folder}/pom2.xml ${project_folder}/pom.xml

	cd $SCRIPT_PATH2

	mvn -q -f ${project_folder}/pom.xml clean generate-sources
	echo "Project mutated with mutator '${mutator}'"

	rm ${project_folder}/pom.xml
	mv ${project_folder}/pom.xml.temp ${project_folder}/pom.xml

	# Modified sources has been generated
	# Project is retablished
done
echo "#   Mutants has been built"
