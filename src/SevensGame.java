import java.util.ArrayList; // ArrayList 자료구조를 사용하기 위한 임포트
import java.util.Collections; // 컬렉션을 다루는 유틸리티 클래스 사용을 위한 임포트
import java.util.Random; // 랜덤 숫자 생성을 위한 클래스 임포트
import java.util.Scanner; // 사용자 입력을 받기 위한 Scanner 클래스 임포트

// 게임에 사용되는 플레이어 클래스
abstract class Player {
    protected String name;
    protected ArrayList<Integer> hand; // 플레이어의 손에 있는 카드를 저장하는 리스트
    protected static final int MAX_DRAW_COUNT = 7;// 한 게임에서 플레이어가 카드를 뽑을 수 있는 최대 횟수

    //  플레이어 객체 생성자
    public Player(String name) {
        this.name = name;
        this.hand = new ArrayList<>();  //카드 목록 초기화
    }

    // 플레이어 이름 반환 메소드
    public String getName() {
        return name;
    }

    // 카드를 손에 추가하는 메소드
    public void addCard(int card) {
        hand.add(card);
    }

    // 플레이어의 손에 있는 카드 출력 메소드
    public void displayHand() {
        System.out.print(name + "의 덱 : ");
        for (int card : hand) {
            System.out.print(card + " ");
        }
        System.out.println();
    }

    // 손에서 특정 카드를 제거하는 메소드
    public void removeCard(int card) {
        hand.remove(Integer.valueOf(card));
    }

    // 손에 있는 카드 수를 반환하는 메소드
    public int getHandSize() {
        return hand.size();
    }

    // 손에 있는 모든 카드를 반환하는 메소드
    public ArrayList<Integer> getHand() {
        return hand;
    }

    // 각 플레이어가 턴을 수행하는 메소드 (추상 메소드로 선언, 구현 필요)
    //Player 클래스에서 정의된 메소드로, 구체적인 구현 내용 없이 선언만 되어 있음
    /*
     * 플레이어 행동의 다형성 보장: Player의 하위 클래스들은 각각 이 메소드를 자신의 게임 로직에 맞게 오버라이드(재정의)해야 합니다. 예를 들어, HumanPlayer1 클래스와 ComputerPlayer1 클래스는 사용자 입력과 AI 로직에 따라 다르게 playTurn 메소드를 구현합니다.

게임 흐름의 일관성 유지: 모든 플레이어 타입(사람, 컴퓨터 등)이 공통의 인터페이스를 사용함으로써, SevensGame 클래스는 플레이어의 타입에 상관없이 통일된 방식으로 플레이어의 턴을 처리할 수 있습니다.

확장성 제공: 새로운 플레이어 타입을 추가할 때, 기존 게임 로직을 변경하지 않고 새로운 클래스를 만들어 playTurn 메소드만 적절히 구현하면 쉽게 새 플레이어 타입을 게임에 통합할 수 있습니다.

결국, 이 추상 메소드는 Player 클래스를 상속받는 모든 클래스가 게임의 규칙에 따라 자신의 턴을 어떻게 수행할지를 구체적으로 정의하도록 요구함으로써, 게임의 다양한 플레이어 행동을 유연하게 관리할 수 있는 구조를 만듭니다. 이를 통해 코드의 재사용성과 유지보수성이 향상됩니다.
     */
    public abstract void playTurn(SevensGame game);
}

// 플레이어 클래스 구현, HumanPlayer1 클래스는 Player 클래스를 구현
class HumanPlayer1 extends Player {
    public HumanPlayer1(String name) {
        super(name);
    }

    // 플레이어의 턴을 구현하는 메소드
    @Override
    public void playTurn(SevensGame game) {
        System.out.println("\n" + name + "의 턴 ");
        displayHand();

        Scanner sc = new Scanner(System.in);
        System.out.print("낼 카드 선택(또는 1장 뽑기 위해 0 입력) : ");
        int cardToPlay = sc.nextInt();

        // 0을 입력받으면 카드 한 장을 뽑음
        if (cardToPlay == 0) {
            game.drawCard(this);
            System.out.println(name + "가 카드를 뽑습니다.");
        } else if (cardToPlay % 7 == 0) { // 7의 배수 카드인 경우
            if (!hand.contains(cardToPlay)) {
                System.out.println("오류입니다! 손에 없는 카드를 선택하셨습니다.");
            } else {
                removeCard(cardToPlay);
                System.out.println(name + "가 " + cardToPlay + "를 선택하여 제거하였습니다.");
            }
        } else {
            System.out.println("오류입니다! 7의 배수인 카드를 내야 합니다.");
        }

        // 최대 뽑기 횟수에 도달하면 승자 결정
        if (game.getPlayerDrawCount() == MAX_DRAW_COUNT) {
            game.findWinner();
        }
    }
}

// ComputerPlayer1 클래스도 Player 클래스를 구현
class ComputerPlayer1 extends Player {
    public ComputerPlayer1(String name) {
        super(name);
    }

    // 컴퓨터의 턴을 구현하는 메소드
    @Override
    public void playTurn(SevensGame game) {
        System.out.println("\n" + name + "의 턴");
        displayHand();

        boolean sevenMultipleCardExists = false;
        int cardToPlay = 0;
        for (int card : hand) {
            if (card % 7 == 0) {
                sevenMultipleCardExists = true;
                cardToPlay = card;
                break;
            }
        }

        // 7의 배수 카드가 있다면 카드를 내고 제거
        if (sevenMultipleCardExists) {
            removeCard(cardToPlay);
            System.out.println(name + "가 " + cardToPlay + "를 선택하여 제거하였습니다.");
        } else {
            // 최대 뽑기 횟수에 도달하지 않았다면 카드를 뽑음
            if (game.getComputerDrawCount() < MAX_DRAW_COUNT) {
                game.drawCard(this);
                System.out.println(name + "가 카드를 뽑습니다.");
                game.incrementComputerDrawCount();
            } else {
                System.out.println(name + "가 최대 드로우 한계에 도달했습니다.");
            }
        }

        // 컴퓨터가 카드를 7번 뽑으면 게임 종료
        if (game.getComputerDrawCount() == MAX_DRAW_COUNT) {
            game.findWinner();
        }
    }
}

// 게임 클래스
public class SevensGame extends GameIntroduction{
    @Override
    public void introGame() {
        System.out.println(" 이 게임은 3레벨의 게임입니다\r\n" + //
                        "\r\n" + //
                        "- 'Sevensgame'은 '7의 배수'를 중심으로 플레이어와 컴퓨터가 카드를 선택하고 매 턴마다 카드를 뽑는 게임입니다.\r\n" + //
                        "\r\n" + //
                        "1. 게임이 시작되면 7장의 카드가 주어집니다.\r\n" + //
                        "2. 플레이어는 턴마다 7의 배수인 카드를 선택하여 제거를 할 수 있습니다.\r\n" + //
                        "3. 7의 배수가 없는 경우 0번 눌러 카드를 뽑을 수 있습니다.\r\n" + //
                        "4. 게임이 종료되었을 때에 카드의 수로 승패를 가릅니다\r\n" + //
                        "\r\n" + //
                        "게임을 시작하겠습니다. ");
    }
    private Player player; // 게임에 참가하는 플레이어 객체
    private Player computer; // 게임에 참가하는 플레이어 객체
    protected int playerDrawCount; // 플레이어가 카드를 뽑은 횟수
    protected int computerDrawCount; // 컴퓨터가 카드를 뽑은 횟수
    protected static final int MAX_DRAW_COUNT = 7; // 최대 카드 뽑기 횟수

    // 게임 생성자
    public SevensGame() {
        player = new HumanPlayer1("플레이어");
        computer = new ComputerPlayer1("컴퓨터");
        playerDrawCount = 0;
        computerDrawCount = 0;
    }

    // 게임을 시작하는 메소드
    public void startGame() {
        // 카드를 나눠줌
        dealCards();

        // 게임이 끝날 때까지 반복
        while (!isGameOver()) {
            // 각 플레이어의 턴 수행
            player.playTurn(this);
            if (!isGameOver()) {
                computer.playTurn(this);
            }
        }
    }

    // 카드를 나눠주는 메소드
    private void dealCards() {
        ArrayList<Integer> deck = new ArrayList<>();
        for (int i = 6; i <= 50; i++) {
            deck.add(i);
        }
        for (int i = 0; i < 7; i++) {
            deck.add(7); // 7의 배수 카드 추가
        }
        Collections.shuffle(deck);
        // 컴퓨터에게 8장, 플레이어에게 5장의 카드를 나눠줌
        for (int i = 0; i < 9; i++) {
            computer.addCard(deck.remove(0));
        }
        for (int i = 0; i < 5; i++) {
            player.addCard(deck.remove(0));
        }
    }

    // 카드를 뽑는 메소드
    public void drawCard(Player player) {
        ArrayList<Integer> deck = new ArrayList<>();
        for (int i = 1; i <= 52; i++) {
            if (!player.getHand().contains(i)) {
                deck.add(i);
            }
        }

        // 덱이 비어있으면 드로우할 수 없음
        if (deck.isEmpty()) {
            System.out.println("덱이 비었습니다! 드로우할 카드가 없습니다.");
            return;
        }

        
        // 랜덤하게 카드를 하나 뽑아 손에 추가
        Random rand = new Random();
        int drawnCardIndex = rand.nextInt(deck.size());
        int drawnCard = deck.get(drawnCardIndex);
        player.addCard(drawnCard);

        // 플레이어의 카드 뽑기 횟수 증가
        if (player instanceof HumanPlayer1) {
            playerDrawCount++;
        } else if (player instanceof ComputerPlayer1) {
            computerDrawCount++;
        }
    }

    // 게임이 끝났는지 확인하는 메소드
    private boolean isGameOver() {
        // 한 플레이어의 손이 비었거나, 둘 다 최대 뽑기 횟수에 도달했을 때 게임 오버
        return player.getHandSize() == 0 || computer.getHandSize() == 0 || (playerDrawCount >= MAX_DRAW_COUNT && computerDrawCount >= MAX_DRAW_COUNT);
    }

    // 승자를 결정하는 메소드
    public void findWinner() {
        if (player.getHandSize() == 0) {
            System.out.println("최종 승자는 " + player.getName() + "입니다!");
        } else if (computer.getHandSize() == 0) {
            System.out.println("최종 승자는 " + computer.getName() + "입니다!");
        } else if (playerDrawCount >= MAX_DRAW_COUNT && computerDrawCount >= MAX_DRAW_COUNT) {

            if (player.getHandSize() < computer.getHandSize()) {
                System.out.println("최종 승자는 " + player.getName() + "입니다!");
            } else if (player.getHandSize() > computer.getHandSize()) {
                System.out.println("최종 승자는 " + computer.getName() + "입니다!");
            } else {
                System.out.println("승자는 없습니다!");
            }
        }
    }

    // 플레이어가 카드를 뽑은 횟수를 반환하는 메소드
    public int getPlayerDrawCount() {
        return playerDrawCount;
    }

    // 컴퓨터가 카드를 뽑은 횟수를 반환하는 메소드
    public int getComputerDrawCount() {
        return computerDrawCount;
    }

    // 컴퓨터가 카드를 뽑은 횟수를 증가시키는 메소드
    public void incrementComputerDrawCount() {
        computerDrawCount++;
    }

    // 메인 메소드: 프로그램 실행 지점
    public static void main(String[] args) {
        SevensGame sevensGame = new SevensGame();
        sevensGame.introGame();
        sevensGame.startGame();
    }
}
