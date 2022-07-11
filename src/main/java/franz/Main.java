package franz;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Main {
    public static void main(String[] args) {
        List<File> imageList = new ArrayList<>();
        File outDir = new File("./outdir/");
        if (outDir.isDirectory() || outDir.mkdirs()) {
            try {
                Files.walkFileTree(Path.of("/mnt/d/SavesSortieren/save_SEA_DISC/Bilder bis 2011 Sicherung/Pictures"), new SimpleFileVisitor<>() {
                /*
                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    System.out.println(dir);
                    return FileVisitResult.CONTINUE;
                }
                 */

                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        String name = file.getFileName().toString();
                        try {
                            Files.probeContentType(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        if (name.endsWith(".eps") ||
                                name.endsWith(".EPS") ||
                                name.endsWith(".raw") ||
                                name.endsWith(".RAW") ||
                                name.endsWith(".cr2") ||
                                name.endsWith(".CR2") ||
                                name.endsWith(".net") ||
                                name.endsWith(".NET") ||
                                name.endsWith(".orf") ||
                                name.endsWith(".ORF") ||
                                name.endsWith(".sr2") ||
                                name.endsWith(".SR2") ||
                                name.endsWith(".gif") ||
                                name.endsWith(".GIF") ||
                                name.endsWith(".bmp") ||
                                name.endsWith(".BMP") ||
                                name.endsWith(".tif") ||
                                name.endsWith(".TIF") ||
                                name.endsWith(".tiff") ||
                                name.endsWith(".TIFF") ||
                                name.endsWith(".jpg") ||
                                name.endsWith(".JPG") ||
                                name.endsWith(".png") ||
                                name.endsWith(".PNG")) {
                            imageList.add(file.toFile());
                        }
                        return FileVisitResult.CONTINUE;
                    }

                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Map<ByteArrWrapper, File> hashMap = new ConcurrentHashMap<>();

            imageList.parallelStream().forEach(file -> {
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
                    ByteArrWrapper w = new ByteArrWrapper(messageDigest.digest(Files.readAllBytes(file.toPath())));
                    File f = hashMap.put(w, file);
                    if (f != null) {
                        System.out.println("Duplikat: " + file + "\t" + f + "\t" + w);
                    }
                } catch (IOException | NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }
            });

            hashMap.forEach((b, f) -> {
                try {
                    Calendar c = Calendar.getInstance();
                    c.setTime(new Date(Files.readAttributes(f.toPath(), BasicFileAttributes.class).creationTime().toMillis()));
                    Path dir = Path.of(outDir.getPath() + "/" + c.get(Calendar.YEAR) + "-" + c.get(Calendar.MONTH));
                    if (Files.exists(dir)) {
                        if (!Files.isDirectory(dir)) {
                            throw new RuntimeException("Required output directory could not be created or used because the file already exists and is no directory. Please resolve.  " + dir);
                        }
                    } else {
                        Files.createDirectory(dir);
                    }
                    Path outPath = Path.of(dir + "/" + f.getName());
                    int counter = 1;
                    while (Files.exists(outPath)) {
                        StringBuilder str = new StringBuilder();
                        String[] arr = f.getName().split("\\.");
                        for (int i = 0; i < arr.length - 1; i++) {
                            str.append(arr[i]).append(".");
                        }
                        str.append("_").append(counter).append(".").append(arr[arr.length - 1]);
                        outPath = Path.of(dir + "/" + str);
                        counter++;
                    }
                    Files.copy(Path.of(f.toURI()), outPath, StandardCopyOption.COPY_ATTRIBUTES);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}