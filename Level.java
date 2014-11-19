public class Level
{
    private int alienCount;
    private Game game;

    public Level(Game game) {
        this.game = game;
    }
    
    public int getAlienCount() {
        return alienCount;
    }
    
    public void level1() {
        alienCount = 0;
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                Entity alien = new AlienEntity(game,"res/alien - Copy.gif",100+(x*50),(50)+row*30);
                game.addToEntities(alien);
                alienCount++;
            }
        }
    }
    
    public void level2() {     
        alienCount = 0;
        for (int row=0;row<5;row++) {
            for (int x=0;x<12;x++) {
                Entity alien = new AlienEntity(game,"res/alien.gif",100+(x*50),(50)+row*30);
                game.addToEntities(alien);
                alienCount++;
            }
        }
    }
    
    public void level3() {       
        alienCount = 0;
        for (int row=0;row<5;row++) {
            for (int x=0;x<4;x++) {
                Entity alien = new AlienEntity(game,"res/alien.gif",100+(x*50),(50)+row*30);
                game.addToEntities(alien);
                alienCount++;
            }
            for (int x=8;x<12;x++) {
                Entity alien2 = new AlienEntity(game,"res/alien.gif",100+(x*50),(50)+row*30);
                game.addToEntities(alien2);
                alienCount++;
            }
        }
        Entity ship = new MotherShipEntity(game, "res/motherShip.gif", 300, 50);
        game.addToEntities(ship);
        alienCount++;
    }
    
    public void level4() {        
        alienCount = 0;
        Entity ship = new MotherShipEntity(game, "res/motherShip.gif", 100, 50);
        game.addToEntities(ship);
        alienCount++;
        for (int row=0;row<5;row++) {
            for (int x=4;x<8;x++) {
                Entity alien = new AlienEntity(game,"res/alien.gif",100+(x*50),(50)+row*30);
                game.addToEntities(alien);
                alienCount++;
            }
        }        
        Entity ship2 = new MotherShipEntity(game, "res/motherShip.gif", 500, 50);
        game.addToEntities(ship2);
        alienCount++;
    }
    
    public void level5() {
        alienCount = 0;
        Entity ship1 = new MotherShipEntity(game, "res/motherShip.gif", 100, 50);
        game.addToEntities(ship1);
        alienCount++;
        Entity ship2 = new MotherShipEntity(game, "res/motherShip.gif", 300, 50);
        game.addToEntities(ship2);
        alienCount++;        
        Entity ship3 = new MotherShipEntity(game, "res/motherShip.gif", 500, 50);
        game.addToEntities(ship3);
        alienCount++;
    }
    
    public void level6() {     
        alienCount = 0;
        Entity boss = new BossEntity(game, "res/bossyboss.gif", 100, 50);
        game.addToEntities(boss);
        alienCount++;
    }
}