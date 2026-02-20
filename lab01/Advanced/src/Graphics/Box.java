package Graphics;

public class Box {
    private Vector2 stangaSus;
    private Vector2 stangaJos;
    private Vector2 dreaptaSus;
    private Vector2 dreaptaJos;

    public void detectBox(Screen n){
        int size = n.getSize();

        int minX = size, maxX = -1, minY = size, maxY = -1;
        boolean found = false;

        for(int i=0; i < size; i++){
            for(int j=0; j < size; j++){
                if(n.getElem(i,j) != 0){
                    stangaSus = new Vector2(j,i);
                    if(j < minX) minX = j;
                    if(i < minY) minY = i;
                    found = true;
                }
            }
        }

        for(int i=size-1; i >= 0; i--){
            for(int j=0; j < size; j++){
                if(n.getElem(i,j) != 0){
                    stangaJos = new Vector2(j,i);
                    if(j < minX) minX = j;
                    if(i > maxY) maxY = i;
                }
            }
        }

        for(int i=0; i < size; i++){
            for(int j=size-1; j >= 0 ; j--){
                if(n.getElem(i,j) != 0){
                    dreaptaSus = new Vector2(j,i);
                    if(j > maxX) maxX = j;
                    if(i < minY) minY = i;
                }
            }
        }

        for(int i=size-1; i >= 0; i--){
            for(int j=size-1; j >= 0; j--){
                if(n.getElem(i,j) != 0){
                    dreaptaJos = new Vector2(j,i);
                    if(j > maxX) maxX = j;
                    if(i > maxY) maxY = i;
                }
            }
        }

        if(!found) return;

        int stanga = minX;
        int dreapta = maxX;
        int sus = minY;
        int jos = maxY;

        for(int i=0; i < size; i++){
            for(int j=0; j < size; j++){
                boolean marginaOrizontala = (i == sus || i == jos) && (j >= stanga && j <= dreapta);
                boolean marginaVerticala = (j == stanga || j == dreapta) && (i >= sus && i <= jos);

                if(marginaOrizontala || marginaVerticala){
                    n.setElem(i,j,2);
                }
            }
        }
    }
}