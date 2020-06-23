#/bin/bash

microservices=("authentication" "portal")
mainVerticles=("com.kyra.auth.AuthVerticle" "com.kyra.portal.PortalVerticle")

build() {
  echo "Building microservice: $1, mainVerticle: $2"
  docker-compose run --rm gradle gradle :$1:clean :$1:assemble -PmainVerticle=$2
}

launch() {
  echo "Launching microservice: $1"
  docker-compose up -d $1
}

launchAll() {
  for ((idx=0; idx<${#microservices[@]}; ++idx))
  do
    build ${microservices[idx]} ${mainVerticles[idx]}
  done

  docker-compose up -d nginx
}

getMicroserviceIndex() {
  for i in "${!microservices[@]}"; do
   if [[ "${microservices[$i]}" = $1 ]]; then
       echo "${i}";
   fi
done
}

develop() {
  idx=$(getMicroserviceIndex $1)
  microservice=${microservices[idx]}
  verticle=${mainVerticles[idx]}
  docker stop $microservice
  build $microservice $verticle
  docker-compose up $microservice
}


POSITIONAL=()
while [[ $# -gt 0 ]]
do
key="$1"

case $key in
    -l|--launch)
      launchAll
      shift
    ;;
    -d|--develop)
      develop $2
      shift
    ;;
    *)
      exit
esac
done
set -- "${POSITIONAL[@]}" # restore positional parameters
