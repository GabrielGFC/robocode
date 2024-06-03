package robopasta;

import robocode.*;
import java.awt.*;

public class kader_wolkenstein_02 extends AdvancedRobot {
    int direcaoMovimento = 2; // Variável que armazena a direção de movimento do robô
    double energia = 100; // Energia inicial do robô
    int tirosAcertados = 0; // Contador de tiros acertados

    public void run() {
        setAdjustRadarForRobotTurn(true); // Mantém o radar parado enquanto o robô gira
        setAdjustGunForRobotTurn(true); // Mantém a arma parada enquanto o robô gira
        turnRadarRightRadians(Double.POSITIVE_INFINITY); // Gira o radar infinitamente para a direita
    }

    public void onScannedRobot(ScannedRobotEvent evento) {
        double anguloAbsoluto = evento.getBearingRadians() + getHeadingRadians(); //calcula o ângulo do robo inimigo
        double velocidadeLateral = evento.getVelocity() * Math.sin(evento.getHeadingRadians() - anguloAbsoluto); //calcula a velocidade lateral do robo inimigo
        double quantidadeGiroArma;

        setTurnRadarLeftRadians(getRadarTurnRemainingRadians()); //ajusta para continuar rastreando o robo inimigo

		//altera aleatoriamente a velocidade maxima do robo
        if(Math.random() > 0.9){
            setMaxVelocity((12 * Math.random()) + 12);
        }

		//se a distância para o robo inimigo for maior que 150...
        if (evento.getDistance() > 150) {
            quantidadeGiroArma = robocode.util.Utils.normalRelativeAngle(anguloAbsoluto - getGunHeadingRadians() + velocidadeLateral / 22); //calcula a quantidade que a arma deve girar
            setTurnGunRightRadians(quantidadeGiroArma); //gira a arma
            setTurnRightRadians(robocode.util.Utils.normalRelativeAngle(anguloAbsoluto - getHeadingRadians() + velocidadeLateral / getVelocity())); //gira o robo na direção do inimigo
            setAhead((evento.getDistance() - 120) * direcaoMovimento); //move para frente
            setFire(3); //dispara
        } else { //se a distancia para o robo inimigo for menor ou igual a 150
            quantidadeGiroArma = robocode.util.Utils.normalRelativeAngle(anguloAbsoluto - getGunHeadingRadians() + velocidadeLateral / 15); //quantidade que a arma deve girar
            setTurnGunRightRadians(quantidadeGiroArma); //gira a arma
            setTurnLeft(-90 - evento.getBearing()); //gira o robo perpendicular ao inimigo 
            setAhead((evento.getDistance() - 120) * direcaoMovimento); //move para frente
            setFire(3); //dispara
        }
    }

    public void onBulletHit(BulletHitEvent event) {
        energia += 5; // Recupera mais energia ao acertar um tiro
        tirosAcertados++; // Incrementa o contador de tiros acertados
    }

    public void onHitWall(HitWallEvent evento){
        direcaoMovimento = -direcaoMovimento;  // Inverte a direção de movimento do robô ao colidir com a parede
    }
}
