import java.util.Random;
import java.util.Scanner;

public class CasinoWar extends GameIntroduction{
    @Override
    public void introGame() {
        System.out.println("이 게임은 1레벨의 게임입니다.\r\n" + //
                        "\r\n" + //
                        "- 'CasinoWar'은 플레이어와 컴퓨터가 랜덤의 카드 숫자 1개씩 뽑아 서로의 숫자 크기를 비교하여 승패를 가르는 게임입니다. \r\n" + //
                        "\r\n" + //
                        "1. 총 5판으로 이루어져 있고 각 게임의 승패로 최종 승패를 가립니다. \r\n" + //
                        "\r\n" + //
                        "게임을 시작하겠습니다. ");
    }
    public static void main(String[] args) throws Exception{
        CasinoWar casinoWar = new CasinoWar();
        casinoWar.introGame();
        Scanner sc = new Scanner(System.in);
        Random random = new Random();

        int playercount = 0;
        int computercount = 0;

        for(int i = 0; i < 5; i++) {
            System.out.println("카드를 뽑기 위해 엔터키를 눌러주세요.");
            sc.nextLine();

            // 플레이어와 컴퓨터의 카드 뽑기(2부터 14(에이스)까지의 랜덤 숫자)
            int playercard = random.nextInt(13) + 2;
            int computercard = random.nextInt(13) + 2;

            System.out.println("당신의 카드 : " + playercard);
            System.out.println("컴퓨터의 카드 : " + computercard + "\n");

            if(playercard > computercard){
                // 사용자의 카드가 더 클 경우
                System.out.println("당신의 승리입니다!(남은 횟수 : " + (4 - i) + ")\n");
                playercount++;
            }
            else if(playercard < computercard){
                // 컴퓨터의 카드가 더 클 경우
                System.out.println("컴퓨터의 승리입니다!(남은 횟수 : " + (4 - i) + ")\n");
                computercount++;
            }
            else{
                // 무승부인 경우
                System.out.println("무승부입니다!(남은 횟수 : " + (4 - i) + ")\n");
            }

        }

        if(playercount > computercount){
            System.out.println("최종 승자는 당신입니다!");
            WinLose.winlose = 1;
        }
        else if(playercount < computercount){
            System.out.println("최종 승자는 컴퓨터입니다!");
            WinLose.winlose = 0;
        }
        else{
            System.out.println("최종 승자는 없습니다!");
            WinLose.winlose = 2;
        }
    }
}