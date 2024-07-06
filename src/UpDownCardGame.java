import java.util.Scanner;

public class UpDownCardGame extends GameIntroduction {
    @Override
    public void introGame() {
        System.out.println("이 게임은 2레벨의 게임입니다.\r\n" + //
                        "\r\n" + //
                        "- 'UpDownCardGame'은 0과 50 사이의 숫자 카드를 골라 맞추는 게임입니다.\r\n" + //
                        "\r\n" + //
                        "1. 플레이어는 숫자 카드를 총 5번 추리할 수 있습니다.\r\n" + //
                        "2. 카드를 뽑을 때마다 컴퓨터가 'Up' 또는 'Down'을 출력합니다.\r\n" + //
                        "3. 플레이어가 숫자를 맞추면 게임은 종료됩니다.\r\n" + //
                        "\r\n" + //
                        "게임을 시작하겠습니다.");
    }

    public static void main(String[] args) throws Exception {
        UpDownCardGame upDownCardGame = new UpDownCardGame();
        upDownCardGame.introGame();
        Scanner sc = new Scanner(System.in);

        int numin;
        int high = 50;
        int low = 0;

        System.out.println("0과 50 사이의 숫자 카드 한 장을 랜덤으로 뽑았습니다. 맞춰 보세요!");

        int rannum = (int) (Math.random() * 50);

        for (int i = 0; i < 5; i++) {
            System.out.print("숫자 입력 : ");

            numin = sc.nextInt();

            if(numin > high || numin < low){
                System.out.println("범위 내에 없는 숫자입니다. 다른 숫자를 입력해 주세요.(남은 횟수 : " + (4 - i) +")");
            } else if (numin < rannum) {
                System.out.println("UP");
                low = numin;
                System.out.println(low + "~" + high);
                System.out.println("남은 횟수 : " + (4 - i));
            } else if (numin > rannum) {
                System.out.println("DOWN");
                high = numin;
                System.out.println(low + "~" + high);
                System.out.println("남은 횟수 : " + (4 - i));
            } else {
                System.out.println("숫자를 맞췄습니다. 당신의 승리!");
                WinLose.winlose = 1;
                break;
            }

            if (i == 4) {
                System.out.println("맞추지 못했습니다. 당신의 패배!");
                WinLose.winlose = 0;
            }
        }
    }
}
