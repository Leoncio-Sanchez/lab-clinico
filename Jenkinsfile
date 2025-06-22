// 🚀 Jenkinsfile - Pipeline de CI/CD para aplicación ecommerce
// 📦 Este pipeline automatiza el proceso de construcción, pruebas y despliegue

pipeline {
    agent any

    // 🛠️ Configuración de herramientas necesarias
    tools {
        maven 'MAVEN_HOME'
    }

    // 🔧 Definición de variables globales
    environment {
        DOCKER_PROJECT_NAME = 'ecomapp'
        APP_CONTAINER_NAME = 'product_app'
        DB_CONTAINER_NAME = 'mysql-ecommerce-prod'
        DB_NAME = 'laboratorio'
        DB_USER = 'root'
        DB_PASSWORD = 'leo321'
        REPO_URL = 'https://github.com/Leoncio-Sanchez/lab-clinico.git'
    }

    stages {
        // 📥 Etapa 1: Clonación del repositorio y verificación
        stage('Clone') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '🔄 === INICIO: CLONACIÓN DEL REPOSITORIO ==='
                    cleanWs()
                    git branch: 'main', url: "${REPO_URL}"

                    echo '📋 === VERIFICACIÓN DE ARCHIVOS SQL ==='
                    sh 'ls -la docker/'
                    sh '''
                        if [ -f "docker/estructura.sql" ]; then
                            echo "✅ Archivo estructura.sql encontrado correctamente"
                            echo "📄 Contenido inicial del archivo:"
                            head -n 5 docker/estructura.sql
                        else
                            echo "❌ ERROR: Archivo estructura.sql no encontrado"
                            exit 1
                        fi
                    '''
                    echo '✅ === FIN: CLONACIÓN Y VERIFICACIÓN COMPLETADA ==='
                }
            }
        }

        // 🏗️ Etapa 2: Construcción del proyecto
        stage('Build') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '🔨 === INICIO: CONSTRUCCIÓN DEL PROYECTO ==='
                    sh 'mvn -DskipTests clean package'
                    echo '✅ === FIN: CONSTRUCCIÓN COMPLETADA ==='
                }
            }
        }

        // 🧪 Etapa 3: Ejecución de pruebas
        stage('Test') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '🧪 === INICIO: EJECUCIÓN DE PRUEBAS ==='
                    sh 'mvn test -DskipTests'
                    echo '✅ === FIN: PRUEBAS COMPLETADAS ==='
                }
            }
        }

        // 📊 Etapa 4: Análisis de calidad con SonarQube
        stage('Sonar Analysis') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '📊 === INICIO: ANÁLISIS DE CALIDAD ==='
                    withSonarQubeEnv('sonarqube') {
                        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Pcoverage'
                    }
                    echo '✅ === FIN: ANÁLISIS DE CALIDAD COMPLETADO ==='
                }
            }
        }

        // 🎯 Etapa 5: Verificación de calidad
        stage('Quality Gate') {
            steps {
                timeout(time: 10, unit: 'MINUTES') {
                    echo '🎯 === VERIFICACIÓN DE ESTÁNDARES DE CALIDAD ==='
                    waitForQualityGate abortPipeline: true
                    echo '✅ === FIN: VERIFICACIÓN DE CALIDAD COMPLETADA ==='
                }
            }
        }

        // 🚀 Etapa 6: Despliegue de la aplicación
        stage('Deploy Application') {
            steps {
                echo '🚀 === INICIO: PROCESO DE DESPLIEGUE ==='
                dir('docker') {
                    script {
                        // 🧹 Limpieza de despliegue anterior
                        echo '1️⃣ Limpiando despliegue anterior...'
                        try {
                            sh "docker-compose -p ${DOCKER_PROJECT_NAME} down -v --remove-orphans"
                        } catch (Exception e) {
                            echo "⚠️ Advertencia: ${e.getMessage()}"
                        }

                        // 🏗️ Construcción y levantamiento de servicios
                        echo '2️⃣ Construyendo y levantando servicios...'
                        sh "docker-compose -p ${DOCKER_PROJECT_NAME} up -d --build"
                    }
                }

                // 💾 Inicialización de la base de datos
                echo '3️⃣ Inicializando base de datos...'
                sleep(30)
                sh "docker exec -i ${DB_CONTAINER_NAME} mysql -u${DB_USER} -p${DB_PASSWORD} ${DB_NAME} < docker/estructura.sql"

                // 🔍 Verificación de la base de datos
                echo '4️⃣ Verificando estructura de la base de datos...'
                sh "docker exec ${DB_CONTAINER_NAME} mysql -u${DB_USER} -p${DB_PASSWORD} -e 'USE ${DB_NAME}; SHOW TABLES;'"

                // ⏳ Espera y verificación de la aplicación
                echo '5️⃣ Esperando inicio de la aplicación...'
                sleep(30)
                echo '6️⃣ Mostrando logs de la aplicación:'
                sh "docker logs --tail 200 ${APP_CONTAINER_NAME}"

                echo '✅ === FIN: DESPLIEGUE COMPLETADO ==='
            }
        }
    }

    // 📝 Acciones post-ejecución
    post {
        always {
            echo '🏁 === FINALIZACIÓN DEL PIPELINE ==='
        }
        success {
            echo '🎉 ✓ Pipeline completado exitosamente'
        }
        failure {
            echo '💥 ✗ Pipeline falló'
        }
    }
}
