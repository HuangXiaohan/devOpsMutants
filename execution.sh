if [ $# -lt 1 ]; then
	echo "Usage : execution <folder>"
  echo "<folder> should be a path to a Maven project to which we would like to apply all our mutators"
  echo "[--windows] if you are on windows and emulating bash, you need to use this argument to use DOS path"
	exit 1
fi

if [ $# -eq 2 ]; then
    if [ $2 == "--windows" ]; then
        windows=true
    fi
fi

project_folder=${1%/}

SCRIPT_PATH="$(cd "$(dirname "$0")" && pwd)" # Répertoire du script
PROJECT_PATH="$(cd "$(dirname "$1")" && pwd)" # Répertoire du script 'build_mutants.sh'

echo "#   Going to mutate and execute tests of the maven project in folder '$(basename ${project_folder})'..."

$SCRIPT_PATH/clean_repertory.sh

if [ "$windows" == "true" ]; then
    $SCRIPT_PATH/build_mutants.sh $PROJECT_PATH/$(basename ${project_folder}) --windows
    $SCRIPT_PATH/launch_tests.sh --windows
else
    $SCRIPT_PATH/build_mutants.sh $PROJECT_PATH/$(basename ${project_folder})
    $SCRIPT_PATH/launch_tests.sh
fi



echo "#   Execution correctly ended"
