import java.util.*;

public class BaseballGame extends GameIntroduction{
    @Override
    public void introGame() {
        System.out.println("이 게임은 5레벨의 게임입니다.\r\n" + //
                        "\r\n" + //
                        "- '야구 숫자 게임'은 플레이어가 3장의 숫자 카드를 뽑아 컴퓨터가 생각한 3자리의 숫자를 맞추는 게임입니다. -\r\n" + //
                        "\r\n" + //
                        "1. 3장의 카드 중 컴퓨터가 생각한 숫자가 있지만 자리가 맞지 않으면 볼 컴퓨터가 생각한 숫자가 있으며 자리도 맞는 숫자가 있다면 스트라이크 입니다!\r\n" + //
                        "2. 총 기회는 7번이고 그 안에 맞추지 못한다면 당신의 패배입니다.\r\n" + //
                        "\r\n" + //
                        "게임을 시작하겠습니다. ");
    }
    private static final int NUM_DIGITS = 3; // 야구 숫자는 3자리
    private static final int MAX_ATTEMPTS = 7; // 최대 시도 횟수

    public static void main(String[] args) {
        BaseballGame baseballGame = new BaseballGame();
        baseballGame.introGame();

        // 컴퓨터가 생각하는 랜덤한 숫자 생성
        int[] answer = generateRandomNumber();

        System.out.println("숫자 카드들을 랜덤으로 뽑았습니다. 맞춰 보세요!\n");

        Scanner sc = new Scanner(System.in);

        // 사용자가 정답을 맞출 때까지 또는 최대 시도 횟수를 초과할 때까지 게임 진행
        int attempts = 0;
        boolean gameWon = false;
        while (!gameWon && attempts < MAX_ATTEMPTS) {
            attempts++;
            System.out.print(NUM_DIGITS + "자리의 숫자 카드를 뽑아 붙여서 입력해주세요 : ");
            String guessStr = sc.nextLine();

            if(guessStr.length() != 3){
                System.out.println("잘못된 입력 방식입니다. 다시 입력해 주세요.(남은 시도 횟수 : " + (MAX_ATTEMPTS - attempts) + ")\n");
                continue;
            }

            // 입력받은 숫자를 정수 배열로 변환
            int[] guess = parseGuess(guessStr);

            // 스트라이크와 볼 판정
            int strikes = countStrikes(answer, guess);
            int balls = countBalls(answer, guess);

            // 결과 출력
            if (strikes == NUM_DIGITS) {
                System.out.println("\n축하합니다!! 당신이 이겼습니다 정답 : " + Arrays.toString(answer));
                gameWon = true;
                WinLose.winlose = 1;
            } else {
                System.out.println("스트~라이크 : " + strikes + ", 볼.. : " + balls);
                System.out.println("다시 맞춰보세요!! (남은 시도 횟수 : " + (MAX_ATTEMPTS - attempts) + ")\n");
            }
        }

        // 최대 시도 횟수를 초과하여 게임 종료
        if (!gameWon) {
            System.out.println("아쉽지만 시도 횟수를 초과하여 당신이 패배했습니다.");
            System.out.println("정답은 : " + Arrays.toString(answer) + " 입니다.");
            WinLose.winlose = 0;
        }
    }

    // 랜덤한 3자리 숫자 생성
    private static int[] generateRandomNumber() {
        Random random = new Random();
        int[] number = new int[NUM_DIGITS];
        boolean[] used = new boolean[10]; // 중복 숫자 방지

        for (int i = 0; i < NUM_DIGITS; i++) {
            int digit;
            do {
                digit = random.nextInt(10); // 0부터 9까지의 랜덤 숫자 생성
            } while (digit == 0 || used[digit]); // 숫자가 0이거나 이미 사용된 숫자인 경우 다시 생성

            number[i] = digit;
            used[digit] = true;
        }

        return number;
    }

    // 문자열로 입력받은 숫자를 정수 배열로 변환
    private static int[] parseGuess(String guessStr) {
        int[] guess = new int[NUM_DIGITS];
        for (int i = 0; i < NUM_DIGITS; i++) {
            guess[i] = Character.getNumericValue(guessStr.charAt(i));
        }
        return guess;
    }

    // 스트라이크 개수 계산
    private static int countStrikes(int[] answer, int[] guess) {
        int strikes = 0;
        for (int i = 0; i < NUM_DIGITS; i++) {
            if (answer[i] == guess[i]) {
                strikes++;
            }
        }
        return strikes;
    }

    // 볼 개수 계산
    private static int countBalls(int[] answer, int[] guess) {
        int balls = 0;
        boolean[] matched = new boolean[NUM_DIGITS];

        for (int i = 0; i < NUM_DIGITS; i++) {
            for (int j = 0; j < NUM_DIGITS; j++) {
                if (i != j && answer[i] == guess[j] && !matched[j]) {
                    balls++;
                    matched[j] = true;
                    break;
                }
            }
        }
        return balls;
    }
}