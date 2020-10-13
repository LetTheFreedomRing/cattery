package com.example.cattery.bootstrap;

import com.example.cattery.model.*;
import com.example.cattery.repository.*;
import com.example.cattery.service.BreedImageService;
import com.example.cattery.service.CatImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final CatRepository catRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BreedRepository breedRepository;
    private final CatImageService catImageService;
    private final BreedImageService breedImageService;
    private final RoleRepository roleRepository;
    private final PrivilegeRepository privilegeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(CatRepository catRepository, UserRepository userRepository,
                      CommentRepository commentRepository, BreedRepository breedRepository,
                      CatImageService catImageService, BreedImageService breedImageService,
                      RoleRepository roleRepository, PrivilegeRepository privilegeRepository,
                      PasswordEncoder passwordEncoder) {
        this.catRepository = catRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
        this.breedRepository = breedRepository;
        this.catImageService = catImageService;
        this.breedImageService = breedImageService;
        this.roleRepository = roleRepository;
        this.privilegeRepository = privilegeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        loadData();
        log.debug("Data loaded");
        log.debug("Users count : " + userRepository.count());
        log.debug("Cats count : " + catRepository.count());
        log.debug("Comments count : " + commentRepository.count());
    }



    private void loadData() {
        // create breeds
        Breed british = new Breed();
        british.setName("British shorthair");
        british.setOverview("The British Shorthair is solid and muscular with an easygoing personality. As befits his British heritage, he is slightly reserved, but once he gets to know someone he’s quite affectionate. His short, dense coat comes in many colors and patterns and should be brushed two or three times a week to remove dead hair");
        british.setHistory("When the Romans invaded Britain, they brought cats with them to help protect their food supplies from rodents along the way. The Romans eventually left, but the cats remained behind, conquering a country with only their charm. When the breeding of pedigreed cats became a fad in Victorian England, the British Shorthair " +
                "(known simply as the Shorthair in Britain) was one of the first varieties to be developed. The Longhair came about when breeders made crosses to Persians during World War I. As with so many breeds, British Shorthairs almost died out during World War II, victims of food shortages that left breeders unable to feed their cats. After the war, the breed was revived with crosses to domestic shorthairs, Russian Blues, Persians and other cats. " +
                "The American Cat Association recognized the British Blue in 1967, The International Cat Association in 1979 and the Cat Fanciers Association in 1980. In 2009, TICA recognized the British Longhair as a variety, the only cat association to do so.");
        british.setTemper("They are an easygoing and dignified breed, not as active and playful as many but sweet-natured and devoted to their owners, making them a favourite of animal trainers. They tend to be safe around other pets and children since they will tolerate a fair amount of physical interaction, but as a rule do not like to be picked up or carried. They require only minimal grooming and take well to being kept as indoor-only cats; however, they can be prone to obesity unless care is taken with their diet.");
        british.setCare("The British Shorthair’s short, smooth coat is simple to groom with weekly brushing or combing to remove dead hairs. A bath is rarely necessary.\n" +
                "\n" +
                "Brush the teeth to prevent periodontal disease. Daily dental hygiene is best, but weekly brushing is better than nothing. Trim the nails weekly. Wipe the corners of the eyes with a soft, damp cloth to remove any discharge. Use a separate area of the cloth for each eye so you don’t run the risk of spreading any infection.\n" +
                "\n" +
                "Check the ears weekly. If they look dirty, wipe them out with a cotton ball or soft damp cloth moistened with a 50-50 mixture of cider vinegar and warm water. Avoid using cotton swabs, which can damage the interior of the ear.\n" +
                "\n" +
                "Keep the litter box spotlessly clean. Cats are very particular about bathroom hygiene.\n" +
                "\n" +
                "                  It’s a good idea to keep a British Shorthair as an indoor-only cat to protect him from diseases spread by other cats, attacks by dogs or coyotes, and the other dangers that face cats who go outdoors, such as being hit by a car. British Shorthairs who go outdoors also run the risk of being stolen by someone who would like to have such a beautiful cat without paying for it.");
        british.setImage(breedImageService.getDefaultImageBytes());

        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        Role adminRole = createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        Role userRole = createRoleIfNotFound("ROLE_USER", Collections.singletonList(readPrivilege));

        // create users
        User user1 = new User();
        user1.setName("John");
        user1.setRegistrationDate(LocalDate.now());
        user1.setEmail("user1@somemail.com");
        user1.getRoles().add(adminRole);
        user1.getRoles().add(userRole);
        user1.setEnabled(true);
        user1.setPassword(passwordEncoder.encode("password1"));

        User user2 = new User();
        user2.setName("Mary");
        user2.setRegistrationDate(LocalDate.now());
        user2.setEmail("user2@somemail.com");
        user2.getRoles().add(userRole);
        user2.setEnabled(true);
        user2.setPassword(passwordEncoder.encode("password2"));

        // create cats
        Cat cat1 = new Cat();
        cat1.setName("Mimiko");
        cat1.setBreed(british);
        cat1.setCatClass(CatClass.BREEDING);
        british.getCats().add(cat1);
        cat1.setStatus(CatStatus.AVAILABLE);
        cat1.setColour("black golden spotted");
        cat1.setEms("BRI ny 24");
        cat1.setGender(Gender.MALE);
        cat1.setPrice(330);
        cat1.setBirthDate(LocalDate.of(2019, 2, 22));
        cat1.setLastUpdated(LocalDate.now());
        cat1.getImages().add(catImageService.getDefaultImageBytes());

        Cat cat2 = new Cat();
        cat2.setName("Kim");
        cat2.setBreed(british);
        british.getCats().add(cat2);
        cat2.setBirthDate(LocalDate.of(2013, 3, 17));
        cat2.setLastUpdated(LocalDate.now());
        cat2.setGender(Gender.MALE);
        cat2.setStatus(CatStatus.SOLD);
        cat2.setColour("black golden blotched");
        cat2.setEms("BRI ny 22 64");
        cat2.setCatClass(CatClass.BREEDING);
        cat2.setOwner(user2);
        cat2.setPrice(330);
        cat2.getImages().add(catImageService.getDefaultImageBytes());
        user2.getCats().add(cat2);
        user2.getWishList().add(cat1);

        // create comments
        Comment comment = new Comment();
        comment.setMessage("How cute!");
        comment.setDate(LocalDate.now());
        comment.setUser(user1);
        comment.setCat(cat1);

        breedRepository.save(british);
        userRepository.save(user1);
        userRepository.save(user2);
        catRepository.save(cat1);
        catRepository.save(cat2);
        commentRepository.save(comment);
    }

    @Transactional
    private Privilege createPrivilegeIfNotFound(String name) {
        Optional<Privilege> optionalPrivilege = privilegeRepository.findByName(name);
        if (!optionalPrivilege.isPresent()) {
            Privilege privilege = new Privilege();
            privilege.setName(name);
            return privilegeRepository.save(privilege);
        }
        return optionalPrivilege.get();
    }

    @Transactional
    private Role createRoleIfNotFound(String name, Collection<Privilege> privileges) {

        Optional<Role> optionalRole = roleRepository.findByName(name);
        if (!optionalRole.isPresent()) {
            Role role = new Role();
            role.setName(name);
            role.setPrivileges(privileges);
            return roleRepository.save(role);
        }
        return optionalRole.get();
    }
}
