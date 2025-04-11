import { ChangeEvent, useEffect, useState } from 'react';
import { useAppDispatch, useAppSelector } from '../../app/hooks';
import Button from '../../components/miscs/Button';
import { selectCurrentUser } from '../../features/auth/authSlice';
import GithubButton from '../../components/miscs/GithubButton';
import Stack from '@mui/material/Stack';
import Autocomplete from '@mui/material/Autocomplete';
import TextField from '@mui/material/TextField';
import { Category, fetchCategories, selectCategories } from '../../features/categories/categorySlice';
import { fetchTags, selectTags, Tag } from '../../features/tags/tagSlice';
import { createTheme, ThemeProvider } from '@mui/material';
import { toast } from 'sonner';
import { fetchRepositories, Repository, selectAllRepositoriess, selectRepositoryLoading } from '../../features/repositories/repositorySlice';
import { createGame, Game, updateGame } from '../../features/games/gameSlice';
import { RxExternalLink } from 'react-icons/rx';
import { Link } from 'react-router-dom';

type Props = {
  selectedGame?: Game | null
}

const versionRegex = /^\d+\.\d+\.\d+$/;

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

export default function NewGame({ selectedGame }: Props) {
  const dispatch = useAppDispatch();
  const user = useAppSelector(selectCurrentUser);
  const categories = useAppSelector(selectCategories);
  const tags = useAppSelector(selectTags);
  const repositories = useAppSelector(selectAllRepositoriess);
  const loadingRepositories = useAppSelector(selectRepositoryLoading)

  // Form fields
  const [title, setTitle] = useState('');
  const [description, setDescription] = useState('');
  const [version, setVersion] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<Category | null>(null);
  const [selectedTags, setSelectedTags] = useState<Tag[]>([]);
  const [selectedRepository, setSelectedRepository] = useState<Repository | null>(null);


  // Field-level errors
  const [titleError, setTitleError] = useState('');
  const [descriptionError, setDescriptionError] = useState('');
  const [versionError, setVersionError] = useState('');
  const [categoryError, setCategoryError] = useState('');
  const [tagsError, setTagsError] = useState('');
  const [repositoryError, setRepositoryError] = useState('');

  const handleTitleChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setTitle(value);
    if (!value.trim()) {
      setTitleError("Title is required.");
    } else {
      setTitleError("");
    }
  };
  const handleDescriptionChange = (e: ChangeEvent<HTMLTextAreaElement>) => {
    const value = e.target.value;
    setDescription(value);
    if (!value.trim()) {
      setDescriptionError("Description is required.");
    } else {
      setDescriptionError("");
    }
  };
  const handleVersionChange = (e: ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    setVersion(value);
    if (!value.trim()) {
      setVersionError("Version is required.");
    } else if (!versionRegex.test(value)) {
      setVersionError("Please enter a valid version format (x.x.x)).");
    } else {
      setVersionError("");
    }
  };
  const handleRepositoryChange = (e: React.SyntheticEvent, repository: Repository | null) => {
    setSelectedRepository(repository);
    if (!repository) {
      setRepositoryError("Please select a repository.");
    } else {
      setRepositoryError("");
    }
  };
  const handleCategoryChange = (e: React.SyntheticEvent, category: Category | null) => {
    setSelectedCategory(category);
    if (!category) {
      setCategoryError("Please select a category.");
    } else {
      setCategoryError("");
    }
  };

  const handleTagChange = (e: React.SyntheticEvent, tags: Tag[]) => {
    setSelectedTags(tags)
    if (!tags.length) {
      setTagsError("Please select at least one tag.");
    } else {
      setTagsError("");
    }
  };

  function handleSubmit(e: React.MouseEvent): void {
    e.preventDefault();

    let isValid = true;
    if (!title.trim()) {
      setTitleError("Username is required.");
      isValid = false;
    }
    if (!description.trim()) {
      setDescriptionError("Username is required.");
      isValid = false;
    }
    if (!version.trim()) {
      setVersionError("Username is required.");
      isValid = false;
    } else if (!versionRegex.test(version)) {
      setVersionError("Please enter a valid version format (x.x.x)).");
    }
    if (!selectedCategory) {
      setCategoryError("Please select a category.");
      isValid = false;
    } else {
      setCategoryError("");
    }
    if (!selectedTags.length) {
      setTagsError("Please select at least one tag.");
      isValid = false;
    } else {
      setTagsError("");
    }
    if (!selectedRepository) {
      setRepositoryError("Please select a repository.");
      isValid = false;
    } else {
      setRepositoryError("")
    }

    if (!isValid) return;

    const payload: Game = {
      id: selectedGame?.id,
      title: title,
      description: description,
      version: version,
      repository: selectedRepository!,
      category: selectedCategory!,
      tags: selectedTags!,
    }

    try {
      if (selectedGame) {
        // If there's a selected game, we are updating
        dispatch(updateGame(payload));
        toast.success('Game updated successfully!');
      } else {
        // Otherwise, create a new game
        dispatch(createGame(payload));
        toast.success('Game created successfully!');
      }
      // TODO: Navigate to the created game details 
      // ...
    } catch (err: any) {
      console.error(err);
      if (err && err.details && Array.isArray(err.details)) {
        err.details.forEach((detail: { field: string; error: string }) => {
          if (detail.field === 'title') {
            setTitleError(detail.error);
          }
          if (detail.field === 'description') {
            setDescriptionError(detail.error);
          }
          if (detail.field === 'description') {
            setDescriptionError(detail.error);
          }
          if (detail.field === 'version') {
            setVersionError(detail.error);
          }
        });
      } else {
        toast.error(typeof err === 'string' ? err : err.message || 'Request failed');
      }
    }
  }

  useEffect(() => {
    if (selectedGame) {
      setTitle(selectedGame.title || '');
      setDescription(selectedGame.description || '');
      setVersion(selectedGame.version || '');
      setSelectedCategory(selectedGame.category || null);
      setSelectedTags(selectedGame.tags || []);
      setSelectedRepository(selectedGame.repository || null);
    } else {
      setTitle('');
      setDescription('');
      setVersion('');
      setSelectedCategory(null);
      setSelectedTags([]);
      setSelectedRepository(null);
    }
  }, [selectedGame]);

  useEffect(() => {
    dispatch(fetchCategories())
    dispatch(fetchTags())
    if (user?.installationId) {
      dispatch(fetchRepositories())
    }
  }, [dispatch])

  useEffect(() => {
    if (user?.installationId) {
      dispatch(fetchRepositories())
    }
  }, [dispatch, user])

  return (
    <>
      <div className="flex items-center justify-between mb-4 px-4 pt-4">
        <div className='flex items-center gap-2'>
          <h2 className="text-3xl font-semibold">{selectedGame ? "Game Details" : "Build a new game"}</h2>
          {selectedGame && <Link to={`/games/${selectedGame.id}`} target='_blank'>
            <RxExternalLink className='h-8 w-8' />
          </Link>}
        </div>
        <Button
          onClick={handleSubmit}
          className="!py-1 !px-10 hover:!brightness-200 !bg-gray-50/10 border border-primary"
          title="Build"
          type="submit"
          form="gameForm"
        />
      </div>
      <div className='px-4'>
        <form id="gameForm" className='flex flex-col md:flex-row gap-10'>
          <div className='flex flex-col gap-4 w-full md:w-3/5'>
            <div>
              <label htmlFor="title" className="block mb-1 font-semibold">
                Title:
              </label>
              <input
                autoFocus={true}
                id="title"
                className="w-full rounded border border-primary bg-dashboard-secondary placeholder:text-white/50 px-3 py-2 text-white/90"
                placeholder="Enter a title for your game..."
                value={title}
                onChange={handleTitleChange}
              />
              {titleError && <p className="text-red-500 text-sm">{titleError}</p>}
            </div>

            <div>
              <label htmlFor="description" className="block mb-1 font-semibold">
                Description:
              </label>
              <textarea
                id="description"
                className="w-full rounded border border-primary bg-dashboard-secondary placeholder:text-white/50 px-3 py-2 text-white/90"
                placeholder="A short description of your game..."
                rows={3}
                value={description}
                onChange={handleDescriptionChange}
              />
              {descriptionError && (
                <p className="text-red-500 text-sm">{descriptionError}</p>
              )}
            </div>

            <div>
              <label htmlFor="version" className="block mb-1 font-semibold">
                Version:
              </label>
              <input
                id="version"
                className="w-full rounded border border-primary bg-dashboard-secondary placeholder:text-white/50 px-3 py-2 text-white/90"
                placeholder="e.g. 1.0.0"
                value={version}
                onChange={handleVersionChange}
              />
              {versionError && <p className="text-red-500 text-sm">{versionError}</p>}
            </div>
          </div>
          <div className='w-full flex justify-center md:w-2/5 p-4'>
            {user?.installationId ? (
              <div>
                <ThemeProvider theme={darkTheme}>
                  <Stack spacing={3} sx={{ width: 500 }}>
                    <Autocomplete
                      id="repositories"
                      options={repositories}
                      getOptionLabel={(option) => option.name}
                      value={selectedRepository}
                      onChange={handleRepositoryChange}
                      loading={loadingRepositories}
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          variant="standard"
                          label="Repository"
                          placeholder="Select a repository"
                          error={Boolean(repositoryError)}
                          helperText={repositoryError}
                        />
                      )}
                    />
                    <Autocomplete
                      id="categories"
                      options={categories}
                      getOptionLabel={(option) => option.name}
                      value={selectedCategory}
                      onChange={handleCategoryChange}
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          variant="standard"
                          label="Category"
                          placeholder="Category"
                          error={Boolean(categoryError)}
                          helperText={categoryError || ''}

                        />
                      )}
                    />
                    <Autocomplete
                      multiple
                      id="tags"
                      options={tags}
                      getOptionLabel={(option) => option.name}
                      value={selectedTags}
                      onChange={handleTagChange}
                      renderInput={(params) => (
                        <TextField
                          {...params}
                          variant="standard"
                          label="Tags"
                          placeholder="Tags"
                          error={Boolean(tagsError)}
                          helperText={tagsError || ''}
                        />
                      )}
                    />
                  </Stack>
                </ThemeProvider>
              </div>
            ) : (
              <div className='pt-2'>
                <GithubButton />
              </div>
            )}
          </div>
        </form>
      </div>
    </>
  )
}