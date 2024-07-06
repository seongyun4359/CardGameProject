import java.util.*;

public class OmahaPoker extends GameIntroduction{
    @Override
    public void introGame() {
        System.out.println("이 게임은 4레벨의 게임입니다.\r\n" + //
                        "\r\n" + //
                        "- 'OmahaPoker'는 플레이어와 컴퓨터가 각각 2장의 랜덤 카드를 받고 공용으로 주어지는 5장의 카드와의 플레이어들의 패를 평가하여 승패를 가르는 게임입니다. \r\n" + //
                        "\r\n" + //
                        "1. 플레이어는 1장 또는 2장의 카드를 바꿀 수 있습니다.\r\n" + //
                        "\r\n" + //
                        "게임을 시작하겠습니다. ");
    }
    
    private Deck deck; // 카드 덱
    private HumanPlayer humanPlayer; // 사용자 플레이어
    private ComputerPlayer computerPlayer; // 컴퓨터 플레이어
    public List<Card> communityCards; // 공통 카드(테이블에 나온 카드)

    // OmahaPoker 클래스 생성자
    public OmahaPoker() {
        deck = new Deck(); // 새로운 카드 덱 생성
        humanPlayer = new HumanPlayer(this); // 사용자 플레이어 생성
        computerPlayer = new ComputerPlayer(this); // 컴퓨터 플레이어 생성
        communityCards = new ArrayList<>(); // 공통 카드 리스트 생성
    }

    // 게임 실행 메서드
    public void playGame() {
        deck.shuffle(); // 카드 섞기
        dealCards(); // 카드 나눠주기
        showHands(); // 카드 보여주기
        humanPlayer.discardAndRedraw(deck); // 사용자가 카드 선택하고 새로 뽑기
        computerPlayer.computerAutomaticDraw(deck, communityCards); // 컴퓨터가 자동으로 카드 선택하고 새로 뽑기
        determineWinner(); // 승자 결정하기
    }

    // 카드 나눠주는 메서드
    private void dealCards() {
        humanPlayer.addCard(deck.drawCard()); // 사용자에게 카드 나눠주기
        humanPlayer.addCard(deck.drawCard()); // 사용자에게 카드 나눠주기
        computerPlayer.addCard(deck.drawCard()); // 컴퓨터에게 카드 나눠주기
        computerPlayer.addCard(deck.drawCard()); // 컴퓨터에게 카드 나눠주기
        for (int i = 0; i < 5; i++) { // 공통 카드(테이블에 나온 카드) 나눠주기
            communityCards.add(deck.drawCard());
        }
    }

    // 카드 보여주는 메서드
    private void showHands() {
        System.out.println("플레이어의 패 : " + humanPlayer.getHand()); // 사용자의 카드 보여주기
        System.out.println("공개 카드 : " + communityCards); // 공개 카드(테이블에 나온 카드) 보여주기
    }

    // 승자를 결정하는 메서드
    private void determineWinner() {
        HandRank humanRank = evaluateHand(humanPlayer.getHand(), communityCards); // 사용자의 패 평가
        HandRank computerRank = evaluateHand(computerPlayer.getHand(), communityCards); // 컴퓨터의 패 평가

        // 각 플레이어의 패 출력
        System.out.println("플레이어의 패 : " + humanRank + " - " + humanPlayer.getHand() + " + " + communityCards);
        System.out.println("컴퓨터의 패 : " + computerRank + " - " + computerPlayer.getHand() + " + " + communityCards);

        // 승자 출력
        if (humanRank.compareTo(computerRank) > 0) {
            System.out.println("당신의 승리입니다!");
            WinLose.winlose = 1;
        } else if (humanRank.compareTo(computerRank) < 0) {
            System.out.println("컴퓨터의 승리입니다!");
            WinLose.winlose = 0;
        } else {
            int result = compareHands(humanPlayer.getHand(), computerPlayer.getHand());
            if (result > 0) {
                System.out.println("당신의 승리입니다!");
                WinLose.winlose = 1;
            } else if (result < 0) {
                System.out.println("컴퓨터의 승리입니다!");
                WinLose.winlose = 0;
            } else {
                System.out.println("무승부입니다!");
                WinLose.winlose = 2;
            }
        }
    }

    // 카드 비교 메서드
    private int compareHands(List<Card> hand1, List<Card> hand2) {
        Collections.sort(hand1, Comparator.comparing(Card::getRank).reversed());
        Collections.sort(hand2, Comparator.comparing(Card::getRank).reversed());

        for (int i = 0; i < hand1.size(); i++) {
            Rank rank1 = hand1.get(i).getRank();
            Rank rank2 = hand2.get(i).getRank();
            if (rank1.compareTo(rank2) != 0) {
                return rank1.compareTo(rank2);
            }
        }
        return 0;
    }

    // 패를 평가하는 메서드
    public HandRank evaluateHand(List<Card> hand, List<Card> communityCards) {
        List<Card> allCards = new ArrayList<>(hand);
        allCards.addAll(communityCards);
        Collections.sort(allCards, Comparator.comparing(Card::getRank));

        if (hasRoyalFlush(allCards)) return HandRank.ROYAL_FLUSH;
        else if (hasStraightFlush(allCards)) return HandRank.STRAIGHT_FLUSH;
        else if (hasFourOfAKind(allCards)) return HandRank.FOUR_OF_A_KIND;
        else if (hasFullHouse(allCards)) return HandRank.FULL_HOUSE;
        else if (hasFlush(allCards)) return HandRank.FLUSH;
        else if (hasStraight(allCards)) return HandRank.STRAIGHT;
        else if (hasThreeOfAKind(allCards)) return HandRank.THREE_OF_A_KIND;
        else if (hasTwoPair(allCards)) return HandRank.TWO_PAIR;
        else if (hasOnePair(allCards)) return HandRank.ONE_PAIR;
        else return HandRank.HIGH_CARD;
    }

    // 로열 플러시 판단 메서드
    private boolean hasRoyalFlush(List<Card> allCards) {
        return hasStraightFlush(allCards) && hasAce(allCards);
    }

    // 스트레이트 플러시 판단 메서드
    private boolean hasStraightFlush(List<Card> allCards) {
        for (int i = 0; i <= allCards.size() - 5; i++) {
            if (isFlush(allCards.subList(i, i + 5)) && isStraight(allCards.subList(i, i + 5))) {
                return true;
            }
        }
        return false;
    }

    // 포카드 판단 메서드
    private boolean hasFourOfAKind(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        return rankCount.values().stream().anyMatch(count -> count == 4);
    }

    // 풀하우스 판단 메서드
    private boolean hasFullHouse(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        boolean hasThree = false;
        boolean hasPair = false;
        for (int count : rankCount.values()) {
            if (count == 3) hasThree = true;
            if (count == 2) hasPair = true;
        }
        return hasThree && hasPair;
    }

    // 플러시 판단 메서드
    private boolean hasFlush(List<Card> allCards) {
        Map<Suit, Integer> suitCount = getSuitCount(allCards);
        return suitCount.values().stream().anyMatch(count -> count >= 5);
    }

    // 스트레이트 판단 메서드
    private boolean hasStraight(List<Card> allCards) {
        for (int i = 0; i <= allCards.size() - 5; i++) {
            if (isStraight(allCards.subList(i, i + 5))) {
                return true;
            }
        }
        return false;
    }

    // 쓰리 카드 판단 메서드
    private boolean hasThreeOfAKind(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        return rankCount.values().stream().anyMatch(count -> count == 3);
    }

    // 투페어 판단 메서드
    private boolean hasTwoPair(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        long pairCount = rankCount.values().stream().filter(count -> count == 2).count();
        return pairCount >= 2;
    }

    // 원 페어 판단 메서드
    private boolean hasOnePair(List<Card> allCards) {
        Map<Rank, Integer> rankCount = getRankCount(allCards);
        return rankCount.values().stream().anyMatch(count -> count == 2);
    }

    // 카드 랭크 카운트 메서드
    private Map<Rank, Integer> getRankCount(List<Card> cards) {
        Map<Rank, Integer> rankCount = new HashMap<>();
        for (Card card : cards) {
            Rank rank = card.getRank();
            rankCount.put(rank, rankCount.getOrDefault(rank, 0) + 1);
        }
        return rankCount;
    }

    // 카드 슈트 카운트 메서드
    private Map<Suit, Integer> getSuitCount(List<Card> cards) {
        Map<Suit, Integer> suitCount = new HashMap<>();
        for (Card card : cards) {
            Suit suit = card.getSuit();
            suitCount.put(suit, suitCount.getOrDefault(suit, 0) + 1);
        }
        return suitCount;
    }

    // 플러시 판단 메서드
    private boolean isFlush(List<Card> cards) {
        Suit suit = cards.get(0).getSuit();
        return cards.stream().allMatch(card -> card.getSuit() == suit);
    }

    // 스트레이트 판단 메서드
    private boolean isStraight(List<Card> cards) {
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank().ordinal() + 1 != cards.get(i + 1).getRank().ordinal()) {
                return false;
            }
        }
        return true;
    }

    // 에이스 판단 메서드
    private boolean hasAce(List<Card> cards) {
        return cards.stream().anyMatch(card -> card.getRank() == Rank.ACE);
    }

    // 메인 메서드
    public static void main(String[] args) throws Exception {
        OmahaPoker omahaPoker = new OmahaPoker();
        omahaPoker.introGame();
        OmahaPoker game = new OmahaPoker();
        game.playGame();
    }
}

// 플레이어 클래스
class Player1 {
    private List<Card> hand; // 플레이어의 손에 있는 카드

    // Player1 클래스 생성자
    public Player1() {
        hand = new ArrayList<>();
    }

    // 카드 추가 메서드
    public void addCard(Card card) {
        hand.add(card);
    }

    // 카드 제거 메서드
    public void discardCard(int index) {
        hand.remove(index);
    }

    // 손에 있는 카드 반환 메서드
    public List<Card> getHand() {
        return hand;
    }

    // 손에 있는 카드 설정 메서드
    public void setHand(List<Card> newHand) {
        hand = newHand;
    }
}

// HumanPlayer 클래스
class HumanPlayer extends Player1 {
    private OmahaPoker game; // OmahaPoker 클래스의 인스턴스 변수

    // HumanPlayer 클래스 생성자
    public HumanPlayer(OmahaPoker game) {
        this.game = game;
    }

    // 사용자가 카드를 선택하고 새로 뽑는 메서드
    public void discardAndRedraw(Deck deck) {
        Scanner sc = new Scanner(System.in);

        while(true){
            System.out.print("버릴 카드를 골라주세요. (e.g., 1 2) : ");
            String input = sc.nextLine(); // 사용자 입력 받기

            if(input.equals("")){
                break;
            }
            else if(input.equals("1") || input.equals("2") || input.equals("1 2")){
                String[] indices = input.split(" "); // 입력된 숫자들을 공백을 기준으로 나눠 배열에 저장
                List<Integer> discardIndices = new ArrayList<>();
                for (String index : indices) {
                    discardIndices.add(Integer.parseInt(index) - 1); // 입력된 숫자들을 정수형으로 변환하여 리스트에 추가
                }
                // 선택된 카드 버리기
                Collections.sort(discardIndices, Collections.reverseOrder());
                for (int index : discardIndices) {
                    discardCard(index);
                }
    
                // 새로운 카드 뽑기
                for (int i = 0; i < discardIndices.size(); i++) {
                    addCard(deck.drawCard());
                }
                break;
            }
            else{
                System.out.println("잘못된 입력 방식입니다. 다시 입력해 주세요.");
            }
        }
    }
}

// ComputerPlayer 클래스
class ComputerPlayer extends Player1 {
    private OmahaPoker game; // OmahaPoker 클래스의 인스턴스 변수

    // ComputerPlayer 클래스 생성자
    public ComputerPlayer(OmahaPoker game) {
        this.game = game;
    }

    // 컴퓨터가 자동으로 카드를 선택하고 새로 뽑는 메서드
    public void computerAutomaticDraw(Deck deck, List<Card> communityCards) {
        List<Card> currentHand = getHand(); // 컴퓨터의 현재 카드
        List<Card> newHand = new ArrayList<>(currentHand); // 새로운 카드를 저장할 리스트

        // 컴퓨터의 현재 패를 평가합니다.
        HandRank currentRank = game.evaluateHand(currentHand, communityCards);

        // 각 카드를 버려보고 새 카드를 뽑아서 평가합니다.
        for (int i = 0; i < currentHand.size(); i++) {
            List<Card> testHand = new ArrayList<>(currentHand);
            testHand.remove(i); // 현재 카드를 제거합니다.
            testHand.add(deck.drawCard()); // 새 카드를 뽑습니다.

            HandRank testRank = game.evaluateHand(testHand, communityCards);

            // 새 패가 더 좋은 경우에만 교환합니다.
            if (testRank.compareTo(currentRank) > 0) {
                newHand = testHand;
                currentRank = testRank;
            }
        }

        // 새로운 패로 업데이트합니다.
        setHand(newHand);
    }
}


// 카드 덱 클래스
class Deck {
    private List<Card> cards; // 카드 리스트

    // Deck 클래스 생성자
    public Deck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }

    // 카드 섞는 메서드
    public void shuffle() {
        Collections.shuffle(cards);
    }

    // 카드 뽑는 메서드
    public Card drawCard() {
        return cards.remove(0);
    }
}

// 카드 클래스
class Card {
    private Rank rank; // 카드 랭크
    private Suit suit; // 카드 슈트

    // Card 클래스 생성자
    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    // 랭크 반환 메서드
    public Rank getRank() {
        return rank;
    }

    // 슈트 반환 메서드
    public Suit getSuit() {
        return suit;
    }

    // 카드 정보 문자열 반환 메서드
    @Override
    public String toString() {
        String rankStr;
        switch (rank) {
            case TWO: rankStr = "2"; break;
            case THREE: rankStr = "3"; break;
            case FOUR: rankStr = "4"; break;
            case FIVE: rankStr = "5"; break;
            case SIX: rankStr = "6"; break;
            case SEVEN: rankStr = "7"; break;
            case EIGHT: rankStr = "8"; break;
            case NINE: rankStr = "9"; break;
            case TEN: rankStr = "10"; break;
            case JACK: rankStr = "J"; break;
            case QUEEN: rankStr = "Q"; break;
            case KING: rankStr = "K"; break;
            case ACE: rankStr = "A"; break;
            default: rankStr = ""; break; // 예외 처리
        }

        String suitStr;
        switch (suit) {
            case HEARTS: suitStr = "하트"; break;
            case DIAMONDS: suitStr = "다이아몬드"; break;
            case CLUBS: suitStr = "클로버"; break;
            case SPADES: suitStr = "스페이드"; break;
            default: suitStr = ""; break; // 예외 처리
        }

        return rankStr + " " + suitStr;
    }
}

// 카드 랭크 열거형
enum Rank {
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING, ACE
}

// 카드 슈트 열거형
enum Suit {
    HEARTS, DIAMONDS, CLUBS, SPADES
}

// 패 랭크 열거형
enum HandRank {
    HIGH_CARD, ONE_PAIR, TWO_PAIR, THREE_OF_A_KIND, STRAIGHT, FLUSH, FULL_HOUSE, FOUR_OF_A_KIND, STRAIGHT_FLUSH, ROYAL_FLUSH
}