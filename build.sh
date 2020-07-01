#/bin/bash
set -e

microservices=("account" "portal" "project")
mainVerticles=("com.kyra.account.AccountVerticle" "com.kyra.portal.PortalVerticle", "com.kyra.project.ProjectVerticle")

build() {
  echo "Building microservice: $1, mainVerticle: $2"
  docker-compose run --rm gradle gradle :$1:clean :$1:shadowJar -PmainVerticle=$2
}

launch() {
  echo "Launching microservice: $1"
  docker-compose up -d $1
}

launchAll() {
  docker-compose down
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
  docker stop nginx $microservice
  build $microservice $verticle
  docker-compose up -d nginx && docker logs -f $microservice --since 0s
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
